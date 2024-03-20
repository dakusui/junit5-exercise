package org.example.jqwik;

import java.util.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

class SortListProperty {

	@Property
	void sortingKeepsAllElements(@ForAll List<Integer> original) {
		List<Integer> sorted = new ArrayList<>(original);
		Collections.sort(sorted);
		assertThat(sorted).containsExactlyInAnyOrderElementsOf(original);
	}
}
