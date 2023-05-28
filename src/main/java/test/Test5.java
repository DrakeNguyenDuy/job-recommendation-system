package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.data.text.DelimitedColumnEventFormat;
import org.grouplens.lenskit.data.text.TextEventDAO;
import org.lenskit.data.dao.EventCollectionDAO;
import org.lenskit.data.dao.PrefetchingUserEventDAO;
import org.lenskit.data.entities.TypedName;
import org.lenskit.data.events.Event;
import org.lenskit.data.events.Like;
import org.lenskit.util.io.ObjectStreams;

import contants.AttributesContants;
import contants.SystemConstants;

public class Test5 {
	public static List<Event> i() {
		List<Event> events = new ArrayList<Event>();
		TypedName<?> typeNames[] = { AttributesContants.ID_USER, AttributesContants.ID_ITEM,
				AttributesContants.RATING };
		DelimitedColumnEventFormat dcef = DelimitedColumnEventFormat.create("like");// type event is rating, like and
																					// like-batch
		dcef.setDelimiter(SystemConstants.COMMA);
		dcef.setHeaderLines(0);
		TextEventDAO dao = new TextEventDAO(new File("data/rating.csv"), dcef);
		EventCollectionDAO event = (EventCollectionDAO) EventCollectionDAO
				.create(ObjectStreams.makeList(dao.streamEvents()));
		EventCollectionDAO ed = (EventCollectionDAO) EventCollectionDAO
				.create(ObjectStreams.makeList(dao.streamEvents()));
		PrefetchingUserEventDAO d = new PrefetchingUserEventDAO(ed);
		List<Like> userRatings = d.getEventsForUser(1, Like.class);
		System.out.println(userRatings);
		return events;
	}

	public static void main(String[] args) {
		i();
	}
}
