package outside;

import org.junit.Test;

import java.time.*;
import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.toilelibre.libe.pol.Pol.*;
import static org.toilelibre.libe.pol.Pol.InvocationHelper._do;

public class Tests {

    @Test
    public void simpleTest () {
        use(text("Hello world")).to(_do(System.out::println));
    }

    @Test
    public void formatText () {
        use(the(text("This is a format involving a text %s and a number %d")))
                .alongWith(the(text("test"))).and(a(number(2))).to(String::format).andUseTheResult().to(
                        _do(System.out::println));
    }

    @Test
    public void buildAList () {
        add(a("First Element")).to(a(new ArrayList<>()))
                .and().alsoAdd(a("Second Element")).then()
                .useThatAll().alongWith(some("Third Element")).to(Do::concatenation)
                .andUseTheResult().to(_do(System.out::println));
    }

    @Test
    public void buildLocalDateTimeWithAnotherFluentInterface () {
        OffsetDateTime offsetDateTime =
                use(the(value(2008))).to(Year::of)
                .andUseTheResult().alongWith(the(value(8))).to(Year::atMonth)
                .andUseTheResult().alongWith(the(value(23))).to(YearMonth::atDay)
                .andUseTheResult().alongWith(the(value(14))).and(the(value(25))).to(LocalDate::atTime)
                .andUseTheResult().alongWith(the(ZoneOffset.ofHours(2))).to(LocalDateTime::atOffset).ok();
        System.out.println(offsetDateTime);
    }

    @Test
    public void joinStringValues () {
        use(a("This text")).alongWith(a(" will be displayed ")).and(a("at once."))
                .to(Do::join).andUseTheResult().to(_do(System.out::println));
    }

    @Test
    public void fourArgsCall () {
        use(the(text("a"))).alongWith(the(number(1))).and(somethingTrue()).and(some(new byte[]{0, 1, 0}))
                .to(this::method).andUseTheResult().to(_do(System.out::println));
    }

    @Test
    public void replaceIfIsEmptyListByIfListIsEmpty () {
        if(_a(new ArrayList<>()).isNotEmpty()) {
            fail();
        }
        if((a(newList())).with(some(text("An element"))).isEmpty()) {
            fail();
        }
        if((a(newListOf(String.class))).with(some(text("An element"))).doesNotContain("An element")) {
            fail();
        }
    }


    public String method (String arg0, Number arg1, boolean arg2, byte[] arg3) {
        return arg0 + arg1 + arg2 + arg3;
    }
}
