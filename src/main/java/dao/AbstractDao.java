package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.TypedName;
import org.lenskit.util.io.ObjectStream;

import contants.SystemConstants;

public abstract class AbstractDao {
	/* 
	 * -Input:
	 * TypeName<?> tnArray: danh sách các cột trong file csv
	 * File file: file csv cần đọc
	 * String[] collumnsGet: cột cần lấy giá trị
	 */
	protected List<String[]> getObjectStream(TypedName<?> tnArray[], File file, String[] collunmsGet ) {
		List<String[]> lines = new ArrayList<String[]>();
		TypedName<?> typeNames[] = tnArray;
		TextEntitySource es = new TextEntitySource();
		DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
		format.setDelimiter(SystemConstants.COMMA);
		format.setHeader(true);
		format.addColumns(typeNames);
		es.setFormat(format);
		es.setFile(file.toPath());
		try {
			ObjectStream<Entity> ose = es.openStream();
			for (Entity entity : ose) {
				String[] line = new String[collunmsGet.length];
				for (int i = 0; i < collunmsGet.length; i++) {
					line[i]=entity.get(collunmsGet[i]).toString();
				}
//				String[] line = { entity.get(SystemConstants.ID).toString(),
//						(String) entity.get(SystemConstants.SKILLS), (String) entity.get(SystemConstants.ADDRESS) };
				lines.add(line);
			}
			ose.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
}
