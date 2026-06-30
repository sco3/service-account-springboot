package sco3.acc.common;

import java.util.List;

public class CollectionChecker {

	public static boolean isEmpty(List<?>... lists) {
		if (lists == null) {
			return true;
		}
		for (List<?> list : lists) {
			if (list != null && !list.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasItems(List<?>... lists) {
		return !isEmpty(lists);
	}

}
