package project;


import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.lucene.search.*;


public class Search {

	///improved_tweets.json"
    static final String INDEX_PATH = "/home/onil/eclipse-workspace/MavenWebAPP/indexDir";
    //Requires this format for going line by line 
    static final String JSON_FILE_PATH = "/home/onil/eclipse-workspace/Lucene-Test/src/test/resources/improved_tweets.json";
    //requires this format for reading file all at onces
    //static final String JSON_FILE_PATH = "/data02_aaron.json";
    
    public  void buildIndex(){
        try {
        	LuceneIndex lw = new LuceneIndex(INDEX_PATH, JSON_FILE_PATH);
            lw.createIndex();
            //Check the index has been created successfully
//            Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
//            IndexReader indexReader = DirectoryReader.open(indexDirectory);
//            int numDocs = indexReader.numDocs();
//            //assertEquals(1,numDocs);
//            for ( int i = 0; i < numDocs; i++)
//            {
//                Document document = indexReader.document( i);
//                System.out.println( "d=" +document);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public JSONArray testQuery(String testquery, String querytype) throws IOException, ParseException {
    	Analyzer analyzer = new StandardAnalyzer();    	
    	Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser parser = new QueryParser("content", analyzer);
        
        
        if(querytype.equals("hashtag")) {
        	System.out.println(querytype);
        	analyzer = new WhitespaceAnalyzer();
        	parser = new QueryParser("hashtag", analyzer);
        }
        else if(querytype.equals("location")) {
        	analyzer = new WhitespaceAnalyzer();
        	parser = new QueryParser("location", analyzer);
        }
        else if(querytype.equals("name")){
        	analyzer = new WhitespaceAnalyzer();
        	parser = new QueryParser("name", analyzer);
        }
        else if(querytype.equals("screen_name")) {
        	analyzer = new WhitespaceAnalyzer();
        	parser = new QueryParser("screen_name", analyzer);
        }

        Query query = parser.parse(testquery);
       
        System.out.println(query.toString());
        int topHitCount = 100;
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;
        
        String results = "";
        // Iterate through the results:
        JSONArray tweets= new JSONArray();
        String prev = "";
        for (int rank = 0; rank < hits.length; ++rank) {
        	
        	
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
           
            String cur = hitDoc.get("content");
            JSONObject obj = new JSONObject();
            obj.put("score", hits[rank].score);
            obj.put("screen_name",hitDoc.get("screen_name"));
            obj.put("name",hitDoc.get("name"));
            obj.put("content",hitDoc.get("content"));	
            obj.put("hashtags",hitDoc.get("hastags"));
            obj.put("location",hitDoc.get("location"));
            obj.put("profile_pic",hitDoc.get("profile"));
            
            if(!prev.equals(cur)) {
            	
            	tweets.add(obj);
            }
            prev = cur;
             
        }
        indexReader.close();
        directory.close();
        return tweets;
    }

}
