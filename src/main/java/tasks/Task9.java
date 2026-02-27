package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  /* Не мутируем входной список.
  Раньше remove(0) изменял исходную коллекцию.
  Теперь просто пропускаем первый элемент.*/
  public List<String> getNames(List<Person> persons) {
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .toList();
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  // distinct() лишний, потому что Set сам убирает дубликаты.
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  /* Убрана ручная конкатенация строк через "+".
  через .filter(Objects::nonNull) проверяем, что данные не null.
  Убрано дублирование secondName.
  Корректно собираем ФИО без лишних пробелов.*/
  public String convertPersonToString(Person person) {
    return Stream.of(
            person.secondName(),
            person.firstName(),
            person.middleName()
        )
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }
  // словарь id персоны -> ее имя
  /* Убрано new HashMap<>(1), потому что может приводить к лишнему resize.
  Используем putIfAbsent, чтобы при возможных повторяющихся id
  не перезаписывать уже добавленное значение.*/
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
        .collect(Collectors.toMap(
            Person::id,
            this::convertPersonToString,
            (existing, replacement) -> existing
        ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  /* Был двойной цикл O(n * m).
  Теперь O(n + m) через HashSet.*/
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> set = new HashSet<>(persons1);
    return persons2.stream().anyMatch(set::contains);
  }

  // Посчитать число четных чисел
  /* Убрано поле count
  метод стал чистым.
   */
  public long countEven(Stream<Integer> numbers) {
    return numbers
        .filter(num -> num % 2 == 0)
        .count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  /*
  так происходит потому что мы положили в set числа от 1 до 10000,
  a Integer.hashCode() возвращает само число
  и получается что бакеты заполняются так, что обход идет по возрастанию.
   */
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
