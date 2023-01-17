package classes;

import com.github.kardzhaliyski.Container;
import com.github.kardzhaliyski.ContainerException;
import com.github.kardzhaliyski.classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    Container r;

    @BeforeEach
    public void init() {
        r = new Container();
    }

    @Test
    public void autoInject() throws Exception {
        B inst = r.getInstance(B.class);

        assertNotNull(inst);
        assertNotNull(inst.aField);
    }

    @Test
    public void injectImplementation() throws Exception {
        B inst = r.getInstance(B.class);

        assertNotNull(inst);
        assertNotNull(inst.aField);
    }

    @Test
    public void injectInstance() throws Exception {
        A a = new A();
        r.registerInstance(a);
        B inst = r.getInstance(B.class);

        assertNotNull(inst);
        assertSame(a, inst.aField);
    }

    @Test
    public void injectNamedInstance() throws Exception {
        A a = new A();
        r.registerInstance("iname", a);
        F inst = r.getInstance(F.class);

        assertNotNull(inst);
        assertSame(a, inst.iname);
    }

    @Test
    public void injectStringProperty() throws Exception {
        String email = "name@yahoo.com";
        r.registerInstance("email", email);
        FS inst = r.getInstance(FS.class);

        assertNotNull(inst);
        assertNotNull(inst.email);
        assertSame(inst.email, email);
    }

    @Test
    public void constructorInject() throws Exception {
        E inst = r.getInstance(E.class);

        assertNotNull(inst);
        assertNotNull(inst.aField);
    }

    @Test
    public void injectInterface() throws Exception {
        r.registerImplementation(AI.class, A.class);
        B inst = r.getInstance(B.class);

        assertNotNull(inst);
        assertNotNull(inst.aField);
    }

    @Test
    public void injectDefaultImplementationForInterface() throws Exception {
        DI inst = r.getInstance(DI.class);
        assertNotNull(inst);
    }

    @Test()
    public void injectMissingDefaultImplementationForInterface() throws Exception {
        assertThrows(ContainerException.class, () -> r.getInstance(AI.class));
    }

    @Test
    public void decorateInstance() throws Exception {
        C ci = new C();
        r.decorateInstance(ci);

        assertNotNull(ci.bField);
        assertNotNull(ci.bField.aField);
    }

    @Test
    public void initializer() throws Exception {
        String email = "name@yahoo.com";
        r.registerInstance("email", email);
        FSI inst = r.getInstance(FSI.class);

        assertNotNull(inst);
        assertNotNull(inst.email);
        assertEquals(inst.email, "mailto:" + email);
    }

    @Test
    public void constructorInjectWithNamedParam() throws Exception {
        A a = new A();
        r.registerInstance("aNamedField", a);
        EN inst = r.getInstance(EN.class);

        assertNotNull(inst);
        assertNotNull(inst.aField);
        assertEquals(a, inst.aField);
    }

    @Test
    public void testLazy() throws Exception {
        L instance = r.getInstance(L.class);
        assertNotNull(instance.laField);
        assertEquals(0, instance.laField.num);
        assertEquals("something", instance.laField.doSomething());
        assertEquals(5, instance.laField.num);
    }
}
