package com.mazaiting;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

/**
 * Lucene索引 测试
 * 使用到的Jar包：
 * commons-io-2.5.jar
 * lucene-analyzers-common-7.3.0.jar
 * lucene-core-7.3.0.jar
 * lucene-queryparser-7.3.0.jar
 * 
 * @author mazaiting
 */
public class IndexTest {
	
	/**
	 * 测试创建索引
	 * @throws IOException
	 */
	@Test
	public void createIndexTest() throws IOException {
		// 指定索引库的存放位置(Directory 对象)
		Path path = FileSystems.getDefault().getPath("D:\\distribution\\lucene");
		// 1. 创建Directory对象
		// FSDirectory磁盘存储; Directory 保存索引
		Directory directory = FSDirectory.open(path);
		// 2. 指定分词器
		// 基于复杂的语法来生成语汇单元，该语法能识别E-mail地址、首字母缩写词词、
		// 韩语/汉语/日语等字符、字母数字等，还能完成小写转换和移除停用词
		Analyzer analyzer = new StandardAnalyzer();
		// IndexWriter配置对象
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// 3. 创建IndexWriter对象
		IndexWriter indexWriter = new IndexWriter(directory, config);
		
		// 4. 指定原始文件的目录
		File fileDir = new File("G:\\test");
		// 获取文件夹和文件列表
		File[] fileList = fileDir.listFiles();
		
		// 遍历
		for (File file : fileList) {
			// 判断是否为路径，如果不是路径则执行里面的内容
			if (!file.isDirectory()) {
				// 5. 创建文档对象
				Document document = new Document();
				
				// 文件名称
				// 分词 索引 存储
				String fileName = file.getName();
				Field fileNameField = new TextField("fileName", fileName, Store.YES);
				
				// 文件大小
				// 分词 索引 存储
				long fileSize = FileUtils.sizeOf(file);
				Field fileSizeField = new TextField("fileSize", String.valueOf(fileSize), Store.YES);
				
				// 文件路径
				// 不分词 不索引 存储
				String filePath = file.getPath();
				Field filePathField = new StoredField("filePath", filePath);
				
				// 文件内容
				String fileContent = FileUtils.readFileToString(file, "UTF-8");
				Field fileContentField = new TextField("fileContent", fileContent, Store.YES);

				// 添加字段
				document.add(fileNameField);
				document.add(fileSizeField);
				document.add(filePathField);
				document.add(fileContentField);
				
				// 使用IndexWriter对象将Document对象写入到索引库
				indexWriter.addDocument(document);
			}
		}
		// 关闭IndexWriter对象
		indexWriter.close();
	}	
	
	/**
	 * 查询索引
	 * 步骤：
	 * 	1. 创建一个Directory对象，用于指定索引库存放的位置
	 * 	2. 创建一个IndexReader对象，需要指定Directory对象，用于读取索引库中的文件
	 *  3. 创建一个IndexSearcher对象，需要指定IndexReader对象
	 *  4. 创建一个TermQuery对象，指定查询的域和查询的关键词
	 *  5. 执行查询
	 *  6. 返回查询结果，遍历查询结果并输出
	 *  7. 关闭IndexReader	
	 * @throws IOException 
	 */
	@Test
	public void searchIndexTest() throws IOException {
		// Directory, 指定索引库存放的位置
		Path path = FileSystems.getDefault().getPath("D:\\distribution\\lucene");
		Directory directory = FSDirectory.open(path);
		// IndexReader, 读取索引库中的文件
		IndexReader indexReader = DirectoryReader.open(directory);
		// IndexSearcher, 用于查询
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// TermQuery, 指定查询的域和查询的关键词
		Query query = new TermQuery(new Term("fileName", "java.txt"));
		// 执行查询
		TopDocs topDocs = indexSearcher.search(query, 100);
		// 获取数组
		ScoreDoc[] scoreDocs =  topDocs.scoreDocs;
		System.out.println(scoreDocs.length);
		// 遍历结果文档
		for (ScoreDoc scoreDoc : scoreDocs) {
			// 获取文档id
			int docId = scoreDoc.doc;
			// 通过id从索引中获取对应的文档
			Document document = indexReader.document(docId);
			// 获取文件名称
			String fileName = document.get("fileName");
			// 获取文件路径
			String filePath = document.get("filePath");
			// 获取文件大小
			String fileSize = document.get("fileSize");
			// 获取文件内容
			String fileContent = document.get("fileContent");
			System.out.println("==========================================");
			System.out.println("文件名：" + fileName + "\n"
					+ "文件大小： " + fileSize + "\n"
					+ "文件路径：" + filePath + "\n"
					+ "文件内容：" + fileContent);
			
		}
		// 关闭IndexReader
		indexReader.close();
	}
	
	/**
	 * 查看标准分词器的分词效果
	 * @throws IOException 
	 */
	@Test
	public void analyzerTest() throws IOException {
		// 创建一个标准分词器对象
		Analyzer analyzer = new StandardAnalyzer();
		// 获得TokenStream对象
		// 参数1： 字段名，可以随便给；参数2: 要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("test", 
				"The Spring Framework provides a comprehensive programming and configuration model.");
		// 添加引用，可以获得每个关键字
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 添加一个偏移量的引用，记录了关键词的开始位置及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		// 将指针调整到列表的头部
		tokenStream.reset();
		// 遍历关键词列表，通过incrementToken方法判断列表是否结束
		while (tokenStream.incrementToken()) {
			// 关键词其实位置
			System.out.println("start->" + offsetAttribute.startOffset());
			// 取关键词
			System.out.println(charTermAttribute);
			// 结束位置
			System.out.println("end->" + offsetAttribute.endOffset());			
		}
		// 关闭
		tokenStream.close();
		analyzer.close();
	}
}































