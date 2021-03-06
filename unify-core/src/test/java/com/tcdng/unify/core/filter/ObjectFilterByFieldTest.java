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

package com.tcdng.unify.core.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tcdng.unify.core.criterion.BeginsWithField;
import com.tcdng.unify.core.criterion.CriteriaBuilder;
import com.tcdng.unify.core.criterion.EndsWithField;
import com.tcdng.unify.core.criterion.EqualsField;
import com.tcdng.unify.core.criterion.GreaterField;
import com.tcdng.unify.core.criterion.GreaterOrEqualField;
import com.tcdng.unify.core.criterion.LessField;
import com.tcdng.unify.core.criterion.LessOrEqualField;
import com.tcdng.unify.core.criterion.LikeField;
import com.tcdng.unify.core.criterion.NotBeginWithField;
import com.tcdng.unify.core.criterion.NotEndWithField;
import com.tcdng.unify.core.criterion.NotEqualsField;
import com.tcdng.unify.core.criterion.NotLikeField;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Object filter by field tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ObjectFilterByFieldTest {

    @Test
    public void testFilterEqualsField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new EqualsField("name", "description"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));

        objectFilter = new ObjectFilter(new EqualsField("costPrice", "salesPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterNotEqualsField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new NotEqualsField("name", "description"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));

        objectFilter = new ObjectFilter(new NotEqualsField("costPrice", "salesPrice"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterGreaterThanField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new GreaterField("salesPrice", "costPrice"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterGreaterThanEqualField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 19.25);
        Product b = new Product("hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 42.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new GreaterOrEqualField("salesPrice", "costPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterLessThanField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new LessField("salesPrice", "costPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterLessThanEqualField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 19.25);
        Product b = new Product("hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 42.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new LessOrEqualField("salesPrice", "costPrice"));
        assertTrue(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterLikeField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new LikeField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterNotLikeField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "Red Hat", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new NotLikeField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterBeginsWithField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "hat In Red", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new BeginsWithField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterNotBeginWithField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "hat In Red", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new NotBeginWithField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterEndsWithField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "hat In Red", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new EndsWithField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testFilterNotEndWithField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 25.25);
        Product b = new Product("hat", "hat In Red", 60.00, 52.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new NotEndWithField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterShallowAndField() throws Exception {
        Product a = new Product("bandana", "bandana", 20.00, 19.25);
        Product b = new Product("Hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new CriteriaBuilder().beginAnd()
                .addEqualsField("salesPrice", "costPrice").addLikeField("description", "name").endCompound().build());
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testFilterShallowOrField() throws Exception {
        Product a = new Product("bandana", "Bandana", 20.00, 19.25);
        Product b = new Product("Hat", "Red Hat", 60.00, 60.00);
        Product c = new Product("specs", "Blue Spectacles", 45.00, 45.00);
        Product d = new Product("pants", "Wonder pants", 15.00, 17.45);

        ObjectFilter objectFilter = new ObjectFilter(new CriteriaBuilder().beginOr().addEqualsField("salesPrice", "costPrice")
                .addLikeField("description", "name").endCompound().build());
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterEqualsField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new EqualsField("name", "description"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));

        objectFilter = new ObjectFilter(new EqualsField("costPrice", "salesPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterNotEqualsField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new NotEqualsField("name", "description"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));

        objectFilter = new ObjectFilter(new NotEqualsField("costPrice", "salesPrice"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterGreaterThanField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new GreaterField("salesPrice", "costPrice"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterGreaterThanEqualField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 19.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 42.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new GreaterOrEqualField("salesPrice", "costPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterLessThanField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new LessField("salesPrice", "costPrice"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterLessThanEqualField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 19.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 42.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new LessOrEqualField("salesPrice", "costPrice"));
        assertTrue(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterLikeField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new LikeField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterNotLikeField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "Red Hat", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new NotLikeField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterBeginsWithField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "hat In Red", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new BeginsWithField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterNotBeginWithField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "hat In Red", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new NotBeginWithField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterEndsWithField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "hat In Red", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new EndsWithField("description", "name"));
        assertTrue(objectFilter.match(a));
        assertFalse(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterNotEndWithField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 25.25));
        ValueStore b = new BeanValueStore(new Product ("hat", "hat In Red", 60.00, 52.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new NotEndWithField("description", "name"));
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterShallowAndField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "bandana", 20.00, 19.25));
        ValueStore b = new BeanValueStore(new Product ("Hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new CriteriaBuilder().beginAnd()
                .addEqualsField("salesPrice", "costPrice").addLikeField("description", "name").endCompound().build());
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertFalse(objectFilter.match(c));
        assertFalse(objectFilter.match(d));
    }

    @Test
    public void testValueStoreFilterShallowOrField() throws Exception {
        ValueStore a = new BeanValueStore(new Product ("bandana", "Bandana", 20.00, 19.25));
        ValueStore b = new BeanValueStore(new Product ("Hat", "Red Hat", 60.00, 60.00));
        ValueStore c = new BeanValueStore(new Product ("specs", "Blue Spectacles", 45.00, 45.00));
        ValueStore d = new BeanValueStore(new Product ("pants", "Wonder pants", 15.00, 17.45));

        ObjectFilter objectFilter = new ObjectFilter(new CriteriaBuilder().beginOr().addEqualsField("salesPrice", "costPrice")
                .addLikeField("description", "name").endCompound().build());
        assertFalse(objectFilter.match(a));
        assertTrue(objectFilter.match(b));
        assertTrue(objectFilter.match(c));
        assertTrue(objectFilter.match(d));
    }
}
