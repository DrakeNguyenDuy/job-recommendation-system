package test;

import java.io.File;

import org.lenskit.LenskitConfiguration;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.StaticDataSource;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.CommonAttributes;
import org.lenskit.data.entities.EntityType;
import org.lenskit.data.entities.TypedName;

import contants.AttributesContants;

public class Test4 {
	public static void main(String[] args) {
		String singArr = "id1,t�n,skills,address,Feature 4,Feature 5,Feature 6,Feature 7,Feature 8,Feature 9,Feature 10,Feature 11,Feature 12,Feature 13,Feature 14,Feature 15,Feature 16,Feature 17,Feature 18,Feature 19,Feature 20";
		TextEntitySource es = new TextEntitySource("item");
		es.setFile(new File("data\\test.csv").toPath());
		DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
		format.setDelimiter(",");
		format.setEntityType(EntityType.forName("item"));
		format.setHeader(true);
		format.addColumn(AttributesContants.ENTITY_ID);
		format.addColumn(AttributesContants.SKILLS);
		format.addColumn(AttributesContants.ADDRESS);
		es.setFormat(format);
		StaticDataSource sds = new StaticDataSource("sourceentry");
		sds.addSource(es);
		DataAccessObject dao = sds.get();
		System.out.println(dao.getEntityIds(EntityType.forName("item")).size());
		LenskitConfiguration configuration = new LenskitConfiguration();
		
	}
}
