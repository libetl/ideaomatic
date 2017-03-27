package outside;

import org.junit.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.fail;
import static org.toilelibre.libe.pol.Pol.*;
import static org.toilelibre.libe.pol.Pol.InvocationHelper.*;

public class Tests {

    @Test
    public void simpleTest () {
        use(text("Hello world")).to(_do(System.out::println));
    }

    @Test
    public void formatText () {
        use(the(text("This is a format involving a text %s and a number %d")))
                .alongWith(the(text("test"))).and(a(number(2))).to(String::format).and().useTheResult().to(
                        _do(System.out::println));
    }

    @Test
    public void buildAList () {
        add(a("First Element")).to(a(new ArrayList<>()))
                .and().alsoAdd(a("Second Element")).then()
                .useThatAll().alongWith(some("Third Element")).to(Do::concatenation)
                .and().useTheResult().to(_do(System.out::println));
    }

    @Test
    public void buildLocalDateTimeWithAnotherFluentInterface () {
        OffsetDateTime offsetDateTime =
                use(the(value(2008))).to(Year::of)
                .and().useTheResult().alongWith(the(value(8))).to(Year::atMonth)
                .and().useTheResult().alongWith(the(value(23))).to(YearMonth::atDay)
                .and().useTheResult().alongWith(the(value(14))).and(the(value(25))).to(LocalDate::atTime)
                .and().useTheResult().alongWith(the(ZoneOffset.ofHours(2))).to(LocalDateTime::atOffset).ok();
        System.out.println(offsetDateTime);
    }

    @Test
    public void joinStringValues () {
        use(a("This text")).alongWith(a(" will be displayed ")).and(a("at once."))
                .to(Do::join).and().useTheResult().to(_do(System.out::println));
    }

    @Test
    public void fourArgsCall () {
        use(the(text("a"))).alongWith(the(number(1))).and(SOMETHING_TRUE).and(some(new byte[]{0, 1, 0}))
                .to(this::method).and().useTheResult().to(_do(System.out::println));
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
                .to(_do(System.out::println));

        andNow(with(the(natural(0))))
                .apply(natural -> natural + 1)
                .until(weHave(natural -> natural > 50))
            .afterThat()
                .useTheResult()
                .to(_do(System.out::println));

        applySilently(someFunctionFor(someListOf(NATURALS), list -> list.add(new Random().nextInt())))
                     .on(a(new ArrayList<>()))._for(40).times()
                        .and().afterThat().useTheResult().to(_do(System.out::println));
    }

    public String method (String arg0, Number arg1, boolean arg2, byte[] arg3) {
        return arg0 + arg1 + arg2 + arg3;
    }
}
