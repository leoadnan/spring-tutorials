/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcFile;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
import org.apache.hadoop.hive.ql.io.orc.Writer;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Writable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.hadoop.hive.HiveRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@EnableAutoConfiguration
@Configuration
@ImportResource("META-INF/spring/application-context.xml")
public class Application implements CommandLineRunner{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcOperations jdbcOperations;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration hadoopConfiguration;
	
//	@Autowired
//	private HiveRunner hiveRunner;

	@Value("${tweets.input}")
	String input;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

//        ApplicationContext context= SpringApplication.run(Application.class, args);
//        System.out.println("Running hive script..");
//        HiveRunner runner = context.getBean(HiveRunner.class);		
//		runner.call();
//        System.out.println("Script ran successfully..");

    }
    
    public void run(String... strings) throws Exception{
    	System.out.println("Run method  ...");

//    	System.out.println("Running hive script..");
//    	hiveRunner.call();
//    	System.out.println("Script ran successfully..");
    	
		List<String> list = jdbcTemplate.queryForList("show tables",String.class);
		System.out.println("List of Tables :"+list);
		
		/*OrcSerde serde = new OrcSerde();
		
		//Define the struct which will represent each row in the ORC file
	    String typeString = "struct<air_temp:double,station_id:string,lat:double,lon:double>";
	    
	    TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(typeString);
	    ObjectInspector oip = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(typeInfo);
	    List<Object> struct =new ArrayList<Object>(4);
        struct.add(0, new Double(1.1));
        struct.add(1, "1");
        struct.add(2, new Double(1.111));
        struct.add(3, new Double(2.222));
	    
        Writable row = serde.serialize(struct, oip);*/
        
		
		
		ObjectInspector inspector = ObjectInspectorFactory.getReflectionObjectInspector(String.class,ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
		
//		Writer writer = OrcFile.createWriter(new Path("/user/aahmed/store.orc"),
//				OrcFile.writerOptions(hadoopConfiguration).inspector(inspector).stripeSize(1048576/2).bufferSize(1048577).version(OrcFile.Version.V_0_12));
		
		Writer writer = OrcFile.createWriter(new Path("/user/aahmed/store.orc"),OrcFile.writerOptions(hadoopConfiguration).inspector(inspector));
		
		/*OrcSerde serde = new OrcSerde();
		
		//Define the struct which will represent each row in the ORC file
	    String typeString = "struct<store_id:int,store_cd:string,store_nm:string,district_id:int>";
	    
	    TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(typeString);
	    ObjectInspector oip = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(typeInfo);
	    List<Object> struct =new ArrayList<Object>(4);
        struct.add(0, new Integer(1));
        struct.add(1, "store_cd");
        struct.add(2, "store_nm");
        struct.add(3, new Integer(1));
	    
        Writable row = serde.serialize(struct, oip);
        writer.addRow(row);*/
        
		
		Class<?> c = Class.forName("org.apache.hadoop.hive.ql.io.orc.OrcStruct");
		Constructor<?> ctor = c.getDeclaredConstructor(int.class);
		ctor.setAccessible(true);
		Method setFieldValue = c.getDeclaredMethod("setFieldValue", int.class, Object.class);
		setFieldValue.setAccessible(true);

		OrcStruct orcRow = (OrcStruct) ctor.newInstance(1);
		setFieldValue.invoke(orcRow, new Integer(0),"One");
		writer.addRow(orcRow);
		
		
		
		// even OrcStruct is public, its constructor and setFieldValue method are not.
		/*Class<?> c = Class.forName("org.apache.hadoop.hive.ql.io.orc.OrcStruct");
		Constructor<?> ctor = c.getDeclaredConstructor(int.class);
		ctor.setAccessible(true);
		Method setFieldValue = c.getDeclaredMethod("setFieldValue", int.class, Object.class);
		setFieldValue.setAccessible(true);

		for (int j = 0; j < 5; j++) {
			OrcStruct orcRow = (OrcStruct) ctor.newInstance(8);
			for (int i = 0; i < 8; i++)
				setFieldValue.invoke(orcRow, i, "AAA" + j + "BBB" + i);
			writer.addRow(orcRow);
		}*/
		writer.close();

		
		jdbcOperations.execute("LOAD DATA INPATH '/user/aahmed/store.orc' OVERWRITE INTO TABLE emmd_hive.store_orc");
//		ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resourceLoader.getResource("password-analysis.hql"));
		
//		System.out.println("Password records: "+jdbcTemplate.queryForList("select count(*) from passwords",String.class));
		
//		List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from grpshell");
//		System.out.println("Group Shell Results: ");
//		for (Map<String, Object> r : results) {
//			System.out.println(r.get("grpshell.shell") + " : " + r.get("grpshell.count"));
//		}
		
//		System.out.println("Hive Application Completed");
    }
/*
	public void run(String... strings) throws Exception {
		System.out.println("Running Hive task using data from '" + input + "' ...");
		String query = "show tables";
		List<String> list = hive2.queryForList(query,String.class);
		System.out.println("Result:"+list);
		
		query =
				"select tweets.username, tweets.followers " +
				"from " +
				"  (select distinct " +
				"    get_json_object(t.value, '$.user.screen_name') as username, " +
				"    cast(get_json_object(t.value, '$.user.followers_count') as int) as followers " +
				"    from tweetdata t" +
				"  ) tweets " +
				"order by tweets.followers desc limit 10";
		List<Map<String, Object>> results = hive2.queryForList(query);
		System.out.println("Results: ");
		for (Map<String, Object> r : results) {
			System.out.println(r.get("tweets.username") + " : " + r.get("tweets.followers"));
		}
		
		
	}

	@Bean
	DataSourceInitializer hiveInitializer(final DataSource dataSource) {
		final String ddl = "create external table if not exists tweetdata (value STRING) LOCATION '" + input + "'";
		final DataSourceInitializer dsi = new DataSourceInitializer();
	    dsi.setDataSource(dataSource);
		dsi.setDatabasePopulator(new DatabasePopulator() {
			@Override
			public void populate(Connection conn) throws SQLException, ScriptException {
				Statement st = conn.createStatement();
				st.execute(ddl);
				st.close();
			}
		});
		return dsi;
	}
*/
}