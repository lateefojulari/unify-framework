/*
 * Copyright 2018-2020 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tcdng.unify.core.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.tcdng.unify.core.AbstractUnifyComponentTest;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Tests default implementation of runtime java class manager.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RuntimeJavaClassManagerTest extends AbstractUnifyComponentTest {

    private RuntimeJavaClassManager rjcm;

    private final String engHelloSrc = "package com.tcdng.unify.core.runtime;" + "@Language(\"English\")"
            + "public class EnglishHello implements Hello {" + " public String hello() {" + "     return \"Hello\";"
            + " }" + "}";

    private final String naijaHelloSrc = "package com.tcdng.unify.core.runtime;" + "@Language(\"Naija\")"
            + "public class NaijaHello implements Hello {" + " public String hello() {" + "     return \"How now?\";"
            + " }" + "}";

    private final String hausaHelloSrc = "package com.tcdng.unify.core.runtime;" + "@Language(\"Naija\")"
            + "public class NaijaHello implements Hello {" + " public String hello() {" + "     return \"Yaya dai?\";"
            + " }" + "}";

    private final String naijaPersonSrc = "package com.tcdng.unify.core.runtime;"
            + "import com.tcdng.unify.core.runtime.NaijaHello;" + "public class NaijaPerson implements Person {"
            + " private Hello hello;" + " public NaijaPerson() {" + "     hello = new NaijaHello();" + " }"
            + " public String sayHello() {" + "     return hello.hello();" + " }" + "}";

    private final String authorSrc = "package com.tcdng.unify.core.runtime;"
            + "import java.util.List;\n"
            + "import com.tcdng.unify.core.runtime.Author;\n"
            + "public class AuthorImpl implements Author {\n"
            + " private String name;\n"
            + " private List<BookImpl> books;\n"
            + " public void setName(String name) {" + "  this.name = name;" + " };\n"
            + " public String getName() {" + "     return name;" + " };\n"
            + " public void setBooks(List<BookImpl> books) {" + "  this.books = books;" + " };\n"
            + " public List<BookImpl> getBooks() {" + "     return books;" + " };\n"
            + "}";

    private final String bookSrc = "package com.tcdng.unify.core.runtime;"
            + "import com.tcdng.unify.core.runtime.Book;\n"
            + "public class BookImpl implements Book {\n"
            + " private AuthorImpl author;\n"
            + " private String title;\n"
            + " public void setAuthor(Author author) {" + "  this.author = (AuthorImpl) author;" + " };\n"
            + " public Author getAuthor() {" + "     return author;" + " };\n"
            + " public void setTitle(String title) {" + "  this.title = title;" + " };\n"
            + " public String getTitle() {" + "     return title;" + " };\n"
            + "}";

    @Test
    public void testCompileAndLoadClassString() throws Exception {
        rjcm.clearClassLoader();
        Class<?> clazz = rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.EnglishHello", engHelloSrc);
        assertNotNull(clazz);
        assertEquals("com.tcdng.unify.core.runtime.EnglishHello", clazz.getName());
        assertTrue(clazz.isAnnotationPresent(Language.class));
        Language la = clazz.getAnnotation(Language.class);
        assertNotNull(la);
        assertEquals("English", la.value());
    }

    @Test
    public void testCompileAndLoadClassFunction() throws Exception {
        rjcm.clearClassLoader();
        Class<?> clazz = rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", naijaHelloSrc);
        assertNotNull(clazz);
        Hello hello = (Hello) ReflectUtils.newInstance(clazz);
        assertEquals("How now?", hello.hello());
    }

    @Test
    public void testCompileAndLoadClassAsProvider() throws Exception {
        Class<?> clazz1 = rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", naijaHelloSrc);
        assertNotNull(clazz1);
        Class<?> clazz2 = ReflectUtils.classForName("com.tcdng.unify.core.runtime.NaijaHello");
        assertNotNull(clazz2);
        assertSame(clazz1, clazz2);
    }

    @Test
    public void testCompileAndLoadClassStringWithRef() throws Exception {
        rjcm.clearClassLoader();
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", naijaHelloSrc);

        Class<?> clazz = rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaPerson", naijaPersonSrc);
        assertNotNull(clazz);
        assertEquals("com.tcdng.unify.core.runtime.NaijaPerson", clazz.getName());
        Person person = (Person) ReflectUtils.newInstance(clazz);
        assertEquals("How now?", person.sayHello());
    }

    @Test
    public void testCompileAndLoadClassStringWithNewRef() throws Exception {
        rjcm.clearClassLoader();
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", naijaHelloSrc);
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", hausaHelloSrc); // Test child-first

        Class<?> clazz = rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaPerson", naijaPersonSrc);
        assertNotNull(clazz);
        assertEquals("com.tcdng.unify.core.runtime.NaijaPerson", clazz.getName());
        Person person = (Person) ReflectUtils.newInstance(clazz);
        assertEquals("Yaya dai?", person.sayHello());
    }

    @Test
    public void testCompileAndLoadClassesCircular() throws Exception {
        rjcm.clearClassLoader();
        List<JavaClassSource> sourceList = Arrays.asList(
                new JavaClassSource("com.tcdng.unify.core.runtime.AuthorImpl", authorSrc),
                new JavaClassSource("com.tcdng.unify.core.runtime.BookImpl", bookSrc));
        List<Class<?>> clazzList = rjcm.compileAndLoadJavaClasses(sourceList);
        assertNotNull(clazzList);
        assertEquals(2, clazzList.size());
        assertEquals("com.tcdng.unify.core.runtime.AuthorImpl", clazzList.get(0).getName());
        assertEquals("com.tcdng.unify.core.runtime.BookImpl", clazzList.get(1).getName());
        Book book1 = (Book) ReflectUtils.newInstance(clazzList.get(1));
        Book book2 = (Book) ReflectUtils.newInstance(clazzList.get(1));
        book1.setTitle("Leviathan Wakes");
        book2.setTitle("Caliban's War");
        Author author = (Author) ReflectUtils.newInstance(clazzList.get(0));
        author.setName("James S. A. Corey");
        author.setBooks(Arrays.asList(book1, book2));
        
        assertEquals("James S. A. Corey", author.getName());
        List<Book> books = author.getBooks();
        assertNotNull(books);
        assertEquals(2, books.size());
        Book book = books.get(0);
        assertEquals("Leviathan Wakes", book.getTitle());
        book = books.get(1);
        assertEquals("Caliban's War", book.getTitle());
    }

    @Test
    public void testCompileAndLoadClassLoaderDepth() throws Exception {
        rjcm.clearClassLoader();
        assertEquals(0, rjcm.getClassLoaderDepth());
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", naijaHelloSrc);
        assertEquals(1, rjcm.getClassLoaderDepth());
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.EnglishHello", engHelloSrc);
        assertEquals(1, rjcm.getClassLoaderDepth());
        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaPerson", naijaPersonSrc);
        assertEquals(1, rjcm.getClassLoaderDepth());

        rjcm.compileAndLoadJavaClass("com.tcdng.unify.core.runtime.NaijaHello", hausaHelloSrc);
        assertEquals(2, rjcm.getClassLoaderDepth());
    }

    @Override
    protected void onSetup() throws Exception {
        rjcm = (RuntimeJavaClassManager) getComponent(ApplicationComponents.APPLICATION_RUNTIMEJAVACLASSMANAGER);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
