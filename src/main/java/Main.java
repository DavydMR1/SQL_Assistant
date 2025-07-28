import UIs.AdminInterface;
import operators.RequestOperator;
import operators.SQL_Operator;

public class Main {
    public static void main(String[] args) {
        //Пусковой файл и конфигуратор окон
        AdminInterface adminInterface = new AdminInterface();
        RequestOperator requestOperator = new RequestOperator();
        SQL_Operator sql_operator = new SQL_Operator();

        adminInterface.setRequestOperator(requestOperator);
        requestOperator.setSql_operator(sql_operator);

        adminInterface.configureAdminInterface();
    }
}
