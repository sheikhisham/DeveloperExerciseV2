package com.pingidentity.halifax.fuji.domain;

import java.util.Comparator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "firstName", "lastName" })
public class Person implements Comparable<Person> {
	private String firstName;
	private String lastName;
	private String age;
	private String height;
	
	public Person(){}

	@Override
	public int compareTo(Person o) {
		return this.firstName.compareTo(lastName);
	}

	private static final Comparator<Person> byAge = (e1, e2) -> Integer.compare(Integer.parseInt(e1.getAge()), Integer.parseInt(e2.getAge()));
	private static final Comparator<Person> byHeight = (e1, e2) -> Integer.compare(Integer.parseInt(e1.getHeight()), Integer.parseInt(e2.getHeight()));
	private static final Comparator<Person> byFirstName = (e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName());
	private static final Comparator<Person> byLastName = (e1, e2) -> e1.getLastName().compareTo(e2.getLastName());

	public static Comparator<Person> getComparator(String param, String order) {
		Comparator<Person> comp = null;
		if (param == null) {
			comp = byFirstName;
		} else {
			switch (param) {
			case "age":
				comp = byAge;
				break;
			case "height":
				comp = byHeight;
				break;
			case "firstName":
				comp = byFirstName;
				break;
			case "lastName":
				comp = byLastName;
				break;
			default:
				comp = byFirstName;
			}
		}

		if ("desc".equals(order)) {
			return comp.reversed();
		}

		return comp;
	}
}
