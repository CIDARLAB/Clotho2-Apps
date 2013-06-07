package org.clothocad.format.moclo;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ernstl
 */


class CartesianIterable <T> implements Iterable <List <T>> {

    private List <List <T>> lilio;  

    public CartesianIterable (List <List <T>> llo) {
        lilio = llo;
    }

    public Iterator <List <T>> iterator () {
        return new CartesianIterator <T> (lilio);
    }
}
