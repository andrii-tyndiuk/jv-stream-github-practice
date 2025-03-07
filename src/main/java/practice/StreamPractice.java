package practice;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.Candidate;
import model.Cat;
import model.Person;

public class StreamPractice {
    /**
     * Given list of strings where each element contains 1+ numbers:
     * input = {"5,30,100", "0,22,7", ...}
     * return min integer value. One more thing - we're interested in even numbers.
     * If there is no needed data throw RuntimeException with message
     * "Can't get min value from list: < Here is our input 'numbers' >"
     */
    public int findMinEvenNumber(List<String> numbers) {
        if (numbers.isEmpty()) {
            throw new RuntimeException("Can't get min value from list");
        }
        OptionalInt min = numbers
                .stream()
                .map(s -> s.split(","))
                .flatMap(Arrays::stream)
                .map(Integer::parseInt)
                .filter(i -> i % 2 == 0)
                .mapToInt(s -> s)
                .min();
        return min.orElseThrow(() -> new RuntimeException("Can't get min value from list",
                (Throwable) numbers));
    }

    /**
     * Given a List of Integer numbers,
     * return the average of all odd numbers from the list or throw NoSuchElementException.
     * But before that subtract 1 from each element on an odd position (having the odd index).
     */
    public Double getOddNumsAverage(List<Integer> numbers) {
        OptionalDouble average = IntStream.range(0, numbers.size())
                .map(i -> i % 2 != 0 ? numbers.get(i) - 1 : numbers.get(i))
                .filter(n -> n % 2 != 0)
                .average();
        return average.orElseThrow(NoSuchElementException::new);
    }

    /**
     * Given a List of `Person` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new Person(«Victor», 16, Sex.MAN),
     * new Person(«Helen», 42, Sex.WOMAN))`,
     * select from the List only men whose age is from `fromAge` to `toAge` inclusively.
     * <p>
     * Example: select men who can be recruited to army (from 18 to 27 years old inclusively).
     */
    public List<Person> selectMenByAge(List<Person> peopleList, int fromAge, int toAge) {
        return peopleList.stream()
                .filter(person -> person.getAge() >= fromAge && person.getAge() <= toAge
                && person.getSex() == Person.Sex.MAN)
                .collect(Collectors.toList());
    }

    /**
     * Given a List of `Person` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new Person(«Victor», 16, Sex.MAN),
     * new Person(«Helen», 42, Sex.WOMAN))`,
     * select from the List only people whose age is from `fromAge` and to `maleToAge` (for men)
     * or to `femaleToAge` (for women) inclusively.
     * <p>
     * Example: select people of working age
     * (from 18 y.o. and to 60 y.o. for men and to 55 y.o. for women inclusively).
     */
    public List<Person> getWorkablePeople(int fromAge, int femaleToAge,
                                          int maleToAge, List<Person> peopleList) {
        return peopleList.stream()
                .filter(person -> person.getAge() >= fromAge)
                .filter(p -> p.getSex() == Person.Sex.MAN && p.getAge() <= maleToAge
                || p.getSex() == Person.Sex.WOMAN && p.getAge() <= femaleToAge)
                .collect(Collectors.toList());

    }

    /**
     * Given a List of `Person` instances (having `name`, `age`, `sex` and `cats` fields,
     * and each `Cat` having a `name` and `age`),
     * return the names of all cats whose owners are women from `femaleAge` years old inclusively.
     */
    public List<String> getCatsNames(List<Person> peopleList, int femaleAge) {
        return peopleList
                .stream()
                .filter(p -> p.getAge() >= femaleAge
                        && !p.getCats().isEmpty()
                        && p.getSex() == Person.Sex.WOMAN)
                .flatMap(p -> p.getCats().stream())
                .map(Cat::getName)
                .collect(Collectors.toList());
    }

    public List<String> validateCandidates(List<Candidate> candidates) {
        CandidateValidator validator = new CandidateValidator();
        return candidates
                .stream()
                .filter(validator::test)
                .map(Candidate::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
