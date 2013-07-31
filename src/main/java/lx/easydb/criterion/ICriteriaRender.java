package lx.easydb.criterion;

public interface ICriteriaRender {
	String toSqlString(BetweenExpression between);
    String toSqlString(LikeExpression like);
    String toSqlString(IlikeExpression ilike);
    String toSqlString(InExpression inexp);
    String toSqlString(Junction junction);
    String toSqlString(LogicalExpression logicalExpression);
    String toSqlString(NotExpression notExpression);
    String toSqlString(NotNullExpression notNullExpression);
    String toSqlString(NullExpression nullExpression);
    String toSqlString(PlainExpression plainExpression);
    String toSqlString(ValueExpression valueExpression);
    String toSqlString(FieldExpression fieldExpression);
    String toSqlString(Order order);
    String toSqlString(Function function);
    String toSqlString(SimpleExpression simpleExpression);
    String toSqlString(AggregateProjection aggregateProjection);
    String toSqlString(RowCountProjection projection);
    String toSqlString(PropertyProjection projection);
    String toSqlString(ExpressionProjection projection);
}
