package com.mazaiting;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

/**
 * 索引库维护工具类
 * 
 * @author mazaiting
 */
public class LuceneManager {
	/**
	 * 获取IndexWriter对象
	 * 
	 * @return
	 */
	public IndexWriter getIndexWriter() {
		try {
			// 获取索引库路径
			Path path = FileSystems.getDefault().getPath("D:\\distribution\\lucene");
			// 创建索引库字典
			Directory directory = FSDirectory.open(path);
			// 创建分析器
			Analyzer analyzer = new StandardAnalyzer();
			// 创建IndexWriter配置
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			return new IndexWriter(directory, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 全部删除
	 * 
	 * @throws IOException
	 */
	@Test
	public void delAllTest() throws IOException {
		IndexWriter writer = getIndexWriter();
		writer.deleteAll();
		writer.close();
	}

	/**
	 * 根据条件删除
	 * 
	 * @throws IOException
	 */
	@Test
	public void delTest() throws IOException {
		IndexWriter writer = getIndexWriter();
		Query query = new TermQuery(new Term("fileName", "java"));
		writer.deleteDocuments(query);
		writer.close();
	}

	/**
	 * 更新
	 * 
	 * @throws IOException
	 */
	@Test
	public void update() throws IOException {
		IndexWriter writer = getIndexWriter();
		Document document = new Document();
		document.add(new TextField("fileName", "测试文件名", Store.YES));
		document.add(new TextField("fileContent", "测试文件内容", Store.YES));

		// 将lucene删除， 然后添加
		writer.updateDocument(new Term("fileName", "lucene"), document);
		writer.close();
	}

	/**
	 * 获取IndexSearcher
	 * 
	 * @return
	 */
	public IndexSearcher getIndexSearcher() {
		try {
			// 获取索引库路径
			Path path = FileSystems.getDefault().getPath("D:\\distribution\\lucene");
			// 创建索引库字典
			Directory directory = FSDirectory.open(path);
			// 创建索引读取者
			IndexReader indexReader = DirectoryReader.open(directory);
			return new IndexSearcher(indexReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取执行结果
	 * 
	 * @throws IOException
	 */
	public void printResult(IndexSearcher indexSearcher, Query query) throws IOException {
		// 执行查询
		TopDocs topDocs = indexSearcher.search(new TermQuery(new Term("fileName")), 10);
		// 获取数组
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		System.out.println(scoreDocs.length);
		// 遍历结果文档
		for (ScoreDoc scoreDoc : scoreDocs) {
			// 获取文档id
			int docId = scoreDoc.doc;
			// 通过id从索引中获取对应的文档
			Document document = indexSearcher.doc(docId);
			// 获取文件名称
			String fileName = document.get("fileName");
			// 获取文件路径
			String filePath = document.get("filePath");
			// 获取文件大小
			String fileSize = document.get("fileSize");
			// 获取文件内容
			String fileContent = document.get("fileContent");
			System.out.println("==========================================");
			System.out.println("文件名：" + fileName + "\n" + "文件大小： " + fileSize + "\n" + "文件路径：" + filePath + "\n"
					+ "文件内容：" + fileContent);

		}
	}
	
	/**
	 * 查询所有
	 * @throws IOException 
	 */
	@Test
	public void matchAllDocsQueryTest() throws IOException {
		// 获取查询索引对象
		IndexSearcher indexSearcher = getIndexSearcher();
		// 查询所有
		Query query = new MatchAllDocsQuery();
		// 打印结果
		printResult(indexSearcher, query);
		// 关闭资源
		indexSearcher.getIndexReader().close();
	}
	
	/**
	 * 组合查询
	 * @throws IOException 
	 */
	@Test
	public void boolQueryTest() throws IOException {
		// 创建搜索
		IndexSearcher indexSearcher = getIndexSearcher();
		// 创建查询
		Query query1 = new TermQuery(new Term("fileName", "java.txt"));
		Query query2 = new TermQuery(new Term("fileName", "c.txt"));
		// 构建表达式 
		// Occur.MUST: 必须满足此条件, 相当于 and
        // Occur.SHOULD: 应该满足此条件, 但是不满足也可以, 相当于 or
        // Occur.MUST_NOT: 必须不满足, 相当于 not
		BooleanClause clause1 = new BooleanClause(query1, Occur.SHOULD);
		// Build模式创建
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		// 添加表达式
		builder.add(clause1);
		// 添加查询
		builder.add(query2, Occur.SHOULD);
		// 打印
		printResult(indexSearcher, builder.build());
		// 关闭资源
		indexSearcher.getIndexReader().close();
	}
	
	/**
	 * 使用QueryParser解析查询表达式
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void queryParserTest() throws ParseException, IOException {
		IndexSearcher indexSearcher = getIndexSearcher();
		// 创建QueryParser对象，其中参数一：字段名，参数而分词器
		QueryParser queryParser = new QueryParser("fileName", new StandardAnalyzer());
		// 此时：表示使用默认域：fileName
		// 查询fileContent域
		Query query = queryParser.parse("fileContent:apache");
		// 打印
		printResult(indexSearcher, query);
		// 关闭资源
		indexSearcher.getIndexReader().close();
	}
	
	/**
	 * 指定多个默认搜索域
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void multiFieldQueryParser() throws ParseException, IOException {
		IndexSearcher indexSearcher = getIndexSearcher();
		// 指定多个默认搜索域
		String[] fields = {"fileName", "fileContent"};
		// 创建MultiFieldQueryParser对象
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		// 创建查询
		Query query = queryParser.parse("apache");
		// 输出查询条件
		System.out.println(query);
		// 执行查询
		printResult(indexSearcher, query);
		// 关闭资源
		indexSearcher.getIndexReader().close();
	}
}

























