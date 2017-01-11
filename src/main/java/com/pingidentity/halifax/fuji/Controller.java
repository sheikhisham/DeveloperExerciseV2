package com.pingidentity.halifax.fuji;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingidentity.halifax.fuji.domain.PeopleJSON;
import com.pingidentity.halifax.fuji.domain.Person;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class Controller {

	private Set<Person> persons = new HashSet<>();

	public Controller() {
		readJSONFile();			// reading JSON
		readTextFile();			// reading TXT file
	}

	private void readTextFile() {
		Scanner reader = null;
		try {
			reader = new Scanner(new FileInputStream(new File("src/main/resources/people.txt")));

			while (reader.hasNext()) {
				String inputLine = reader.nextLine();
				String[] data = inputLine.split(":");
				Person p = new Person();
				p.setFirstName(data[0]);
				p.setLastName(data[1]);
				p.setAge(data[2]);
				p.setHeight(data[3]);
				persons.add(p);
			}
		} catch (FileNotFoundException e) {
			log.error("Excpetion occured in TextFile conversion : " + e.getMessage());
		} finally {
			reader.close();
		}

	}

	private void readJSONFile() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			PeopleJSON peopleJSON = mapper.readValue(new File("src/main/resources/people.json"), PeopleJSON.class);
			List<Person> personsFromJson = peopleJSON.getPeople();
			persons.addAll(personsFromJson);
		} catch (Exception e) {
			log.error("Excpetion occured in JSON conversion : " + e.getMessage());
		}
	}

	@RequestMapping(value = "/goodluck", method = RequestMethod.GET)
	public String goodLuck() {
		return "Good Luck";
	}

	/**
	 * Rest API to list all persons
	 * 
	 * @return
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ResponseEntity<List<Person>> getPersons(@RequestParam(defaultValue = "firstName") String sortby,
			@RequestParam(defaultValue = "asc") String order) {
		List<Person> sorted = persons.stream().sorted(Person.getComparator(sortby, order)).collect(Collectors.toList());
		return new ResponseEntity<>(sorted, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Rest API to create person
	 * 
	 * @param person
	 */
	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public ResponseEntity<List<Person>> createPerson(@RequestBody Person person)
			throws URISyntaxException {
		boolean createStatus = persons.add(person);
		List<Person> sorted = persons.stream().sorted(Person.getComparator(null, null)).collect(Collectors.toList());
		return new ResponseEntity<>(sorted, new HttpHeaders(), createStatus ? HttpStatus.CREATED : HttpStatus.CONFLICT);
	}
}
