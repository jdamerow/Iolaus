package edu.asu.lerna.iolaus.parser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.FileUtils;

public class EmbeddedNeo4jJavaQuery
{
    private static final String DB_PATH = "db/data/";
    String resultString;
    String columnsString;
    String nodeResult;
    String rows = "";

    public static void main( String[] args )
    {
        EmbeddedNeo4jJavaQuery javaQuery = new EmbeddedNeo4jJavaQuery();
        javaQuery.run();
    }

    void run()
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        System.out.println("Starting insertion");
        
        // START SNIPPET: addData
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        Transaction tx = db.beginTx();
        try
        {
            Node myNode = db.createNode();
            myNode.setProperty( "name", "my node" );
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        // END SNIPPET: addData
        System.out.println("End insertion");
        System.out.println("Starting retrieval");

        // START SNIPPET: execute
        ExecutionEngine engine = new ExecutionEngine( db );
        ExecutionResult result = engine.execute( "start n=node(*) where n.name! = 'my node' return n, n.name" );
        // END SNIPPET: execute
        // START SNIPPET: columns
        List<String> columns = result.columns();
        // END SNIPPET: columns
        // START SNIPPET: items
        Iterator<Node> n_column = result.columnAs( "n" );
        for ( Node node : IteratorUtil.asIterable( n_column ) )
        {
            // note: we're grabbing the name property from the node,
            // not from the n.name in this case.
            nodeResult = node + ": " + node.getProperty( "name" );
        }
        System.out.println("___________________________________");
        System.out.println(nodeResult);
        System.out.println("___________________________________");
        // END SNIPPET: items
        // the result is now empty, get a new one
        result = engine.execute( "start n=node(*) where n.name! = 'my node' return n, n.name" );
        // START SNIPPET: rows
        for ( Map<String, Object> row : result )
        {
            for ( Entry<String, Object> column : row.entrySet() )
            {
                rows += column.getKey() + ": " + column.getValue() + "; ";
            }
            rows += "\n";
        }
        System.out.println(rows);
        // END SNIPPET: rows
        resultString = engine.execute( "start n=node(*) where n.name! = 'my node' return n, n.name" ).dumpToString();
        columnsString = columns.toString();
        db.shutdown();
        System.out.println("end retrieval");
    }
}