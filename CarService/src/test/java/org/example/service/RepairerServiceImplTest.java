package org.example.service;

import junit.framework.Assert;
import org.example.model.Repairer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RepairerServiceImplTest {

    List<Repairer> repairerList = new ArrayList<>();
    private final RepairerServiceImpl repairerService = new RepairerServiceImpl();

    @BeforeEach
    void setUp() {
        Repairer repairer1 = new Repairer("Harry");
        repairer1.setId(0);
        repairerList.add(repairer1);
        Repairer repairer2 = new Repairer("Ivan");
        repairer2.setId(1);
        repairerList.add(repairer2);
        Repairer repairer3 = new Repairer("Oleg");
        repairer3.setId(2);
        repairerList.add(repairer3);
        repairerService.setRepairers(repairerList);
    }

    @Test
    void getById() {
        Assert.assertEquals(repairerList.get(0), repairerService.getById(0));
    }

    @Test
    void add() {
        Repairer repairerToAdd = new Repairer("Tom");
//        repairerService.add(repairerToAdd);
//        Assert.assertEquals(, );
    }

    @Test
    void remove() {
    }

    @Test
    void testToString() {
    }
}