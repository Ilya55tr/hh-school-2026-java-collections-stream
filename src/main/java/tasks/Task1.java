package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

/*
Построение HashMap-O(n),
формирование результата-O(n),
потому что мы один раз проходим по списку id
и получение Person из personMap-O(1)
итоговая сложность-O(n)
*/
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map<Integer, Person> personMap = persons.stream()
        .collect(Collectors.toMap(Person::id, person -> person));
    return personIds.stream()
        .map(personMap::get)
        .collect(Collectors.toList());
  }
}
