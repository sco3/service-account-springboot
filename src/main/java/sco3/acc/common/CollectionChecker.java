package sco3.acc.common;

import java.util.Collection;

public class CollectionChecker {

	public static boolean isEmpty(Collection<?>... lists) {
		if (lists == null) {
			return true;
		}
		for (Collection<?> list : lists) {
			if (list != null && !list.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasItems(Collection<?>... lists) {
		return !isEmpty(lists);
	}

}
