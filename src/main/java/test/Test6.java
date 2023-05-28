package test;

import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.data.text.DelimitedColumnEventFormat;

import contants.SystemConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class Test6 {
	public static void main(String[] args) {
//		Long2ObjectMap<List<String>> mapAllItem = new Long2ObjectOpenHashMap<List<String>>();
//		List<String> l = new ArrayList<String>();
//		mapAllItem.put(1, l);
//		l.add("con");
//		l.add("em");
//		System.out.println(mapAllItem.get(1).contains("con"));
		DelimitedColumnEventFormat dcef = DelimitedColumnEventFormat.create("like");// type event is
		// rating, like and
		// like-batch
		dcef.setDelimiter(SystemConstants.COMMA);
		dcef.setHeaderLines(0);
		System.out.println(dcef.getFields().get(1));
	}
}
