package test;

import java.awt.TextField;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class test1 {
	String[] a = {
			"3, 华为 - 华为电脑, 爆款",
			"4, 华为手机, 旗舰",
			"5, 联想 - Thinkpad, 商务本",
			"6, 联想手机, 自拍神器"
	};
	@Test
	public void test1() throws Exception {
		//存储索引的路径
		File path = new File("e:/dpsy/");
		FSDirectory d = FSDirectory.open(path.toPath());
		//指定分词器
		IndexWriterConfig cfg = new IndexWriterConfig(new SmartChineseAnalyzer());
		//索引输出工具
		IndexWriter writer = new IndexWriter(d, cfg);
		for (String s : a) {
			String[] arr=s.split(",");
			//创建一个文档对象,封装一个文档的字段
			Document doc = new Document();
			doc.add(new LongPoint("id", Long.parseLong(arr[0].trim())));
			doc.add(new StoredField("id", Long.parseLong(arr[0].trim())));
			doc.add(new org.apache.lucene.document.TextField("tittle", arr[1], Store.YES));
			doc.add(new org.apache.lucene.document.TextField("sellpoint", arr[2], Store.YES));
			
			writer.addDocument(doc);
		}
		writer.flush();
		writer.close();
	}
	@Test
	public void test2() throws IOException {
		FSDirectory d = FSDirectory.open(new File("e:/dpsy").toPath());
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(d));
		TermQuery q = new TermQuery(new Term("tittle","华为"));
		TopDocs dosc = searcher.search(q, 20);
		for (ScoreDoc scoreDoc: dosc.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("tittle"));
			System.out.println(doc.get("sellPoint"));
			System.out.println("-----");
		}
	}
}
