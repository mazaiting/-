package com.mazaiting;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * Solr Java Api 测试
 * @author mazaiting
 */
public class SolrTest {
	// Solr链接
	public static final String url = "http://localhost:8983/solr/solr_sample";
	
	/**
	 * 添加Document
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void addDocTest() throws SolrServerException, IOException {
		// 创建Solr客户端
		SolrClient solrClient = new HttpSolrClient.Builder(url).build();
		// 准备Solr文档
		SolrInputDocument document = new SolrInputDocument();
		// 添加字段
		document.addField("id", "022");
		document.addField("name", "mazaiting");
		document.addField("age", "24");
		document.addField("addr", "中国科学院理化研究所");
		
		// 添加文档到Solr
		solrClient.add(document);
		
		// 保存修改
		solrClient.commit();
		System.out.println("Documents added.");
	}
	
	/**
	 * 删除所有文档
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void delAllDocTest() throws SolrServerException, IOException {
		// 创建客户端
		SolrClient solrClient = new HttpSolrClient.Builder(url).build();
		
		// 从Solr中删除文档
		solrClient.deleteByQuery("*");
	
		// 提交
		solrClient.commit();
		System.out.println("Documents deteled");
	}
	
	/**
	 * 检索数据
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void retrieveDataTest() throws SolrServerException, IOException {
		// 创建客户端
		SolrClient solrClient = new HttpSolrClient.Builder(url).build();
		// 创建查询
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery("*:*");
		// 添加查询字段
		query.addField("*");
		// 执行查询
		QueryResponse queryResponse = solrClient.query(query);
		// 获取查询到的结果集
		SolrDocumentList results = queryResponse.getResults();
		System.out.println(results);
		System.out.println(results.get(0));
		System.out.println(results.get(1));
		System.out.println(results.get(2));
		System.out.println(results.get(3));
		System.out.println(results.get(4));
		System.out.println(results.get(5));
		// 提交
		solrClient.commit();
	}
	
	/**
	 * 构面或分组
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void facetTest() throws SolrServerException, IOException {
		// 创建客户端
		SolrClient solrClient = new HttpSolrClient.Builder(url).build();
		// 准备Solr文档
		SolrInputDocument document = new SolrInputDocument();
		// 创建查询
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery("*:*");
		// 设置终止列
		query.setRows(0);
		// 设置构面
		query.addFacetField("author");
		// 创建查询请求
		QueryRequest request = new QueryRequest(query);
		// 创建查询相应
		QueryResponse response = request.process(solrClient);
		// 打印
		System.out.println(response.getFacetFields());
		// 获取构面列表
		List<FacetField> facetFields = response.getFacetFields();
		// 遍历
		for (FacetField facetField : facetFields) {
			// 获取值
			List<Count> values = facetField.getValues();
			for (Count count : values) {
				System.out.println(count.getName() + " : " + count.getCount() 
				+ "[drilldown qry: " + count.getAsFilterQuery());
			}
			System.out.println("Hello");
		}
	}
}































