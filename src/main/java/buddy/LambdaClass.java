package buddy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class LambdaClass implements RequestHandler<RequestDetails, ResponseDetails>
{
    public ResponseDetails handleRequest(RequestDetails requestDetails, Context arg1) {

        // TODO Auto-generated method stub
        ResponseDetails responseDetails = new ResponseDetails();
        try {
            insertDetails(requestDetails, responseDetails);
        } catch (SQLException sqlException) {
            responseDetails.setMessageID("999");
            responseDetails.setMessageReason("Unable to Registor "+sqlException);
        }
        return responseDetails;
    }

    private void insertDetails(RequestDetails requestDetails, ResponseDetails responseDetails) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String query = getquery(requestDetails);
        int responseCode = statement.executeUpdate(query);
        if(1 == responseCode)
        {
            responseDetails.setMessageID(String.valueOf(responseCode));
            responseDetails.setMessageReason("Successfully updated details to AWS RDS");
        }

    }

    private String getquery(RequestDetails requestDetails) {

        String query = "INSERT INTO infosec_learners.learners_list(id,first_name,last_name,email) VALUES (";
        if (requestDetails != null) {
            query = query.concat("'" + requestDetails.getId() + "','" + requestDetails.getFirst_name() + "','"
                    + requestDetails.getLast_name() + "','" + requestDetails.getEmail() + "')");
        }
        System.out.println("the query is "+query);
        return query;
    }

    private Connection getConnection() throws SQLException {
        // TODO Auto-generated method stub
        String url = "jdbc:mysql://db-retool.cymonduqvooq.us-east-2.rds.amazonaws.com:3306";
        String user = System.getenv("databaseUser");
        String password = System.getenv("databasePassword");
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;

    }

}
