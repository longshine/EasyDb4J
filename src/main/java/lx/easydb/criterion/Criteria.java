package lx.easydb.criterion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import lx.easydb.IConnection;
import lx.easydb.IConnectionFactory;
import lx.easydb.ICriteria;
import lx.easydb.MappingException;
import lx.easydb.StringHelper;
import lx.easydb.Types;
import lx.easydb.dialect.MySQLDialect;
import lx.easydb.dialect.PostgreSQLDialect;
import lx.easydb.dialect.function.ISQLFunction;
import lx.easydb.mapping.Column;
import lx.easydb.mapping.Table;

public class Criteria implements ICriteria, ICriteriaRender {
	private List conditions = new ArrayList();
	private IProjection projection;
	private List orders = new ArrayList();
	private LinkedHashMap params = new LinkedHashMap();
	private int offset;
	private int total;
	private String entity;
	private IConnection connection;
	private IConnectionFactory factory;
	private Table table;
	private boolean parameterized = true;

	public Criteria(String entity, IConnection connection, IConnectionFactory factory) {
        this.connection = connection;
        this.factory = factory;
        this.entity = entity;
        this.total = -1;
        this.offset = 0;
		this.table = factory.getMapping().findTable(entity);
	}
	
	public void setParameterized(boolean parameterized) {
		this.parameterized = parameterized;
	}

	public boolean isParameterized() {
		return parameterized;
	}
	
	public ICriteria add(IExpression condition) {
		 conditions.add(condition);
         return this;
	}

	public ICriteria addOrder(Order order) {
		orders.add(order);
        return this;
	}

	public int count() throws SQLException {
		List list = connection.query(Integer.class, toSqlCountString(), params.keySet(), null, params);
		return ((Integer) list.get(0)).intValue();
	}

	public List list() throws SQLException {
		return list(-1, 0);
	}

	public List list(int total, int offset) throws SQLException {
		this.total = total;
        this.offset = offset;
        return connection.query(entity, toSqlString(), params.keySet(), null, params);
	}

	public ICriteria setProjection(IProjection projection) {
		this.projection = projection;
		return this;
	}

	public Object single() throws SQLException {
		List list = list(1, 0);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public String toSqlString() {
		params.clear();
        String sql = generateSelect();
        String orderby = generateOrder();
        if (total >= 0)
            sql = factory.getDialect().getPaging(sql, orderby, total, offset);
        else if (orderby != null)
            sql += " " + orderby;
        //System.out.println(sql);
        return sql;
    }
	
	public String toSqlCountString() {
		params.clear();
        String select = generateSelect();
        StringBuffer sbSql = StringHelper.createBuilder()
             .append("select count(*) from (")
             .append(select)
             .append(") t");
        return sbSql.toString();
    }
	
	private String generateSelect() {
		StringBuffer sbSql = StringHelper.createBuilder();

        if (projection == null) {
			sbSql.append(table.toSqlSelect(factory.getDialect(),factory.getMapping().getCatalog(),
							factory.getMapping().getSchema(), false, null));
		} else {
            sbSql.append("select ")
                .append(projection.render(this))
                .append(" from ")
                .append(table.getQualifiedName(factory.getDialect(),
                		factory.getMapping().getCatalog(), factory.getMapping().getSchema()));
        }

        generateFragment(sbSql, "where", conditions, " AND ");

        if (projection != null && projection.isGrouped())
            sbSql.append(" group by ").append(projection.toGroupString(this));

        return sbSql.toString();
    }
	
	private String generateOrder() {
        if (orders.size() > 0) {
        	StringBuffer sb = StringHelper.createBuilder().append("order by ");
        	boolean append = false;
        	Iterator it = orders.iterator();
        	while (it.hasNext()) {
        		if (append)
        			sb.append(", ");
        		else
        			append = true;
        		Order order = (Order) it.next();
        		sb.append(order.render(this));
        	}
            return sb.toString();
        }
        
        return null;
    }
	
	private void generateFragment(StringBuffer sb, String prefix, List exps, String sep) {
        if (exps.size() > 0) {
            sb.append(' ').append(prefix).append(' ');
            boolean append = false;
        	Iterator it = exps.iterator();
        	while (it.hasNext()) {
        		if (append)
        			sb.append(sep);
        		else
        			append = true;
        		IExpression exp = (IExpression) it.next();
        		sb.append(exp.render(this));
        	}
        }
    }
	
	private String registerParam(Object value) {
		return registerParam("p_" + params.size(), value);
	}

	private String registerParam(String name, Object value) {
		params.put(name, value);
		return factory.getDialect().paramPrefix();
	}

	public String toSqlString(BetweenExpression between) {
		return StringHelper.createBuilder()
			.append(between.getExpression().render(this))
			.append(" between ")
			.append(between.getLower().render(this))
			.append(" and ")
			.append(between.getUpper().render(this))
			.toString();
	}

	public String toSqlString(LikeExpression like) {
		StringBuffer sb = StringHelper.createBuilder();
		
		if (like.isIgnoreCase())
			sb.append(factory.getDialect().getLowercaseFunction())
				.append('(').append(like.getExpression().render(this)).append(')');
		else
			sb.append(like.getExpression().render(this));
		
		sb.append(" like ")
			.append(like.getMatchMode().toMatchString(like.getValue().render(this)));
		
		if (like.getEscapeChar() != null)
			sb.append(" escape \'").append(like.getEscapeChar()).append("\'");
		
		return sb.toString();
	}

	public String toSqlString(IlikeExpression ilike) {
		StringBuffer sb = StringHelper.createBuilder();

        if (factory.getDialect() instanceof PostgreSQLDialect)
            sb.append(ilike.getExpression().render(this))
                .append(" ilike ");
        else
            sb.append(factory.getDialect().getLowercaseFunction())
                .append('(').append(ilike.getExpression().render(this)).append(')')
                .append(" like ");

        return sb.append(ilike.getMatchMode().toMatchString(ilike.getValue().render(this))).toString();
	}

	public String toSqlString(InExpression inexp) {
		StringBuffer sb = StringHelper.createBuilder()
	        .append(inexp.getExpression().render(this))
	        .append(" in (");
		
		IExpression[] ies = inexp.getValues();
		for (int i = 0; i < ies.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(ies[i].render(this));
		}

		return sb.append(')').toString();
	}

	public String toSqlString(Junction junction) {
		if (junction.getExpressions().size() == 0)
            return "1=1";

		StringBuffer sb = StringHelper.createBuilder().append('(');
		
		boolean append = false;
		Iterator it = junction.getExpressions().iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(' ').append(junction.getOp()).append(' ');
			else
				append = true;
			IExpression exp = (IExpression) it.next();
			sb.append(exp.render(this));
		}

        return sb.append(')').toString();
	}

	public String toSqlString(LogicalExpression logical) {
		return StringHelper.createBuilder()
	        .append('(')
	        .append(logical.getLeft().render(this))
	        .append(' ')
	        .append(logical.getOp())
	        .append(' ')
	        .append(logical.getRight().render(this))
	        .append(')')
	        .toString();
	}

	public String toSqlString(NotExpression not) {
		if (factory.getDialect() instanceof MySQLDialect)
            return "not (" + not.getExpression().render(this) + ')';
        else
            return "not " + not.getExpression().render(this);
	}

	public String toSqlString(NotNullExpression notNull) {
		return notNull.getExpression().render(this) + " is not null";
	}

	public String toSqlString(NullExpression nullexp) {
		return nullexp.getExpression().render(this) + " is null";
	}

	public String toSqlString(PlainExpression plain) {
		return plain.toString();
	}

	public String toSqlString(ValueExpression value) {
		if (isParameterized())
            return registerParam(value.getValue());
        else
            return value.toString();
	}

	public String toSqlString(FieldExpression field) {
		Column column = table.findColumnByFieldName(field.getFieldName());
        if (column == null)
            return field.toString();
        else if (field.getTableName() == null)
            return column.getQuotedName(factory.getDialect());
        else
        	return factory.getDialect().quote(field.getTableName()) + "." + column.getQuotedName(factory.getDialect());
	}

	public String toSqlString(Order order) {
		return StringHelper.createBuilder()
	        .append(order.getExpression().render(this))
	        .append((order.isAscending() ? " ASC" : " DESC"))
	        .toString();
	}

	public String toSqlString(Function function) {
		ISQLFunction func = factory.getDialect().findFunction(function.getName());
        if (func == null)
            // TODO throw an exception
            throw new MappingException("Function not found: " + function.getName());
        List list = new ArrayList();
        IExpression[] ies = function.getArgs();
        for (int i = 0; i < ies.length; i++) {
        	list.add(ies[i].render(this));
        }
        return func.render(Types.EMPTY, list, factory);
	}

	public String toSqlString(SimpleExpression simple) {
		return StringHelper.createBuilder()
	        .append('(')
	        .append(simple.getLeft().render(this))
	        .append(' ')
	        .append(simple.getOp())
	        .append(' ')
	        .append(simple.getRight().render(this))
	        .append(')')
	        .toString();
	}

	public String toSqlString(AggregateProjection aggregateProjection) {
		ISQLFunction func = factory.getDialect().findFunction(aggregateProjection.getFunctionName());
        if (func == null)
            // TODO throw an exception
            throw new MappingException("Function not found");
        return alias(func.render(Types.EMPTY, aggregateProjection.buildFunctionParameterList(this),
        		factory), aggregateProjection.getAlias());
    }

	public String toSqlString(RowCountProjection projection) {
		ISQLFunction func = factory.getDialect().findFunction("count");
        if (func == null)
            throw new MappingException("count function not found");
        return alias(func.render(Types.EMPTY, RowCountProjection.ARGS, factory),
        		projection.getAlias());
    }

	public String toSqlString(PropertyProjection propertyProjection) {
		return alias(Clauses.field(propertyProjection.getPropertyName())
				.render(this), propertyProjection.getAlias());
	}

	public String toSqlString(ExpressionProjection projection) {
		return alias(projection.getExpression().render(this), projection.getAlias());
	}
	
	private String alias(String exp, String alias) {
        return (alias == null || alias.length() == 0) ?
        		exp : (exp + " AS " + factory.getDialect().quote(alias));
    }
}
