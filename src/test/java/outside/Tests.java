package outside;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.fail;
import static org.toilelibre.libe.pol.Pol.Do;
import static org.toilelibre.libe.pol.Pol.Get;
import static org.toilelibre.libe.pol.Pol.InvocationHelper.someFunctionFor;
import static org.toilelibre.libe.pol.Pol.InvocationHelper.someListOf;
import static org.toilelibre.libe.pol.Pol.NATURALS;
import static org.toilelibre.libe.pol.Pol.SOMETHING_TRUE;
import static org.toilelibre.libe.pol.Pol.TEXTS;
import static org.toilelibre.libe.pol.Pol._a;
import static org.toilelibre.libe.pol.Pol.a;
import static org.toilelibre.libe.pol.Pol.add;
import static org.toilelibre.libe.pol.Pol.andNow;
import static org.toilelibre.libe.pol.Pol.applySilently;
import static org.toilelibre.libe.pol.Pol.forEach;
import static org.toilelibre.libe.pol.Pol.like;
import static org.toilelibre.libe.pol.Pol.natural;
import static org.toilelibre.libe.pol.Pol.newList;
import static org.toilelibre.libe.pol.Pol.newListOf;
import static org.toilelibre.libe.pol.Pol.number;
import static org.toilelibre.libe.pol.Pol.some;
import static org.toilelibre.libe.pol.Pol.text;
import static org.toilelibre.libe.pol.Pol.the;
import static org.toilelibre.libe.pol.Pol.theList;
import static org.toilelibre.libe.pol.Pol.use;
import static org.toilelibre.libe.pol.Pol.value;
import static org.toilelibre.libe.pol.Pol.weHave;
import static org.toilelibre.libe.pol.Pol.with;
import static org.toilelibre.libe.pol.Pol.withTheList;

public class Tests {

    @Test
    public void simpleTest () {
        use(some(text(like("Hello world")))).to(Do::println);
    }

    @Test
    public void formatText () {
        use(the(text("This is a format involving a text %s and a number %d")))
                .alongWith(the(text("test"))).and(a(number(2))).to(String::format).and().useTheResult().to(
                        Do::println);
    }

    @Test
    public void buildAList () {
        add(a("First Element")).to(a(new ArrayList<>()))
                .and().alsoAdd(a("Second Element")).then()
                .useThatAll().alongWith(some("Third Element")).to(Do::concatenation)
                .and().useTheResult().to(Do::println);
    }

    @Test
    public void buildLocalDateTimeWithAnotherFluentInterface () {
        OffsetDateTime offsetDateTime =
                use(the(value(2008))).to(Get::aYear)
                .and().useTheResult().alongWith(the(value(8))).to(Get::aYearMonth)
                .and().useTheResult().alongWith(the(value(23))).to(Get::aDate)
                .and().useTheResult().alongWith(the(value(14))).and(the(value(25))).to(Get::aDateTime)
                .and().useTheResult().alongWith(the(ZoneOffset.ofHours(2))).to(Get::aDateTimeWithTimezone).ok();
        System.out.println(offsetDateTime);
    }

    @Test
    public void joinStringValues () {
        use(a("This text")).alongWith(a(" will be displayed ")).and(a("at once."))
                .to(Do::join).and().useTheResult().to(Do::println);
    }

    @Test
    public void fourArgsCall () {
        use(the(text("a"))).alongWith(the(number(1))).and(SOMETHING_TRUE).and(some(new byte[]{0, 1, 0}))
                .to(this::method).and().useTheResult().to(Do::println);
    }

    @Test
    public void replaceIfIsEmptyListByIfListIsEmpty () {
        if(_a(new ArrayList<>()).isNotEmpty()) {
            fail();
        }
        if((a(newList())).with(some(text("An element"))).isEmpty()) {
            fail();
        }
        if((a(newListOf(TEXTS))).with(some(text(like("An element")))).doesNotContain("An element")) {
            fail();
        }
    }

    @Test
    public void mergeTwoLists () {
        Collection<String> mergedList = use(a(newListOf(TEXTS)).with(some(text(like("First List Element"))))).alongWith(a(newListOf(TEXTS)).with(some(text(like("Second List Element"))))).to(Do::merge).thatIsAll();
        System.out.println(mergedList);
    }

    @Test
    public void loop () {
        andNow(with(the(text("Ni ! "))))
                .apply(text -> text + "Ni ! ")
                .until(weHave(text -> text.length() > 50))
            .afterThat()
                .useTheResult()
                .to(Do::println);

        andNow(with(the(natural(0))))
                .apply(natural -> natural + 1)
                .until(weHave(natural -> natural > 50))
            .afterThat()
                .useTheResult()
                .to(Do::println);

        applySilently(someFunctionFor(someListOf(NATURALS), list -> list.add(new Random().nextInt())))
                     .on(a(new ArrayList<>()))._for(40).times()
                        .and().afterThat().useTheResult().to(Do::println);

        andNow(with(theList("Alpha", "Bravo", "Charlie", "Delta", "Echo")))
                .apply(forEach(text -> text.substring(0, 1)))
                .onEveryElement().and()
             .afterThat()
                .useTheResult()
                .to(Do::println);

        withTheList("A", "B", "C", "D", "E")
                .loop().apply(forEach(x -> "" + x + x)).onEveryElement()
                .afterThat().useTheResult().to(Do::println);

    }

    @Test
    public void intersection () {
        use(theList("A1", "A2", "C3", "C4")).alongWith(theList("B1", "B2", "C3", "C4")).to(Do::intersection)
                .useTheResult().to(Do::println);
    }

    private String method (String arg0, Number arg1, boolean arg2, byte[] arg3) {
        return arg0 + arg1 + arg2 + new String(arg3);
    }
}
