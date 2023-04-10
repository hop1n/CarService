package org.example.service;

import junit.framework.Assert;
import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.example.constants.ExceptionMessage.INVALID_REPAIRER_ID;

class RepairerServiceImplTest {

    private RepairerServiceImpl repairerService;
    Repairer repairerToAdd = new Repairer("Tom");
    Repairer repairer1 = new Repairer("Harry");
    Repairer repairer2 = new Repairer("Ivan");
    Repairer repairer3 = new Repairer("Oleg");

    @BeforeEach
    void setUp() {
        repairerService = new RepairerServiceImpl();
        repairerService.add(repairer1);
        repairerService.add(repairer2);
        repairerService.add(repairer3);
    }

    @Test
    void getById() {
        Assert.assertEquals(repairer1, repairerService.getById(1));
    }

    @Test
    void add() {
        repairerService.add(repairerToAdd);
        Assert.assertEquals(4, repairerService.getRepairers().size());
        Assert.assertTrue(repairerService.getRepairers().contains(repairerToAdd));
        Assert.assertEquals(repairerService.toString(), "[\n" +
                "Repairer{name='Harry',isAvailable=true,id=1,}, \n" +
                "Repairer{name='Ivan',isAvailable=true,id=2,}, \n" +
                "Repairer{name='Oleg',isAvailable=true,id=3,}, \n" +
                "Repairer{name='Tom',isAvailable=true,id=4,}]");
    }

    @Test
    void remove() {
        repairerService.remove(3);
        Assert.assertEquals(2, repairerService.getRepairers().size());
        Assert.assertFalse(repairerService.getRepairers().contains(repairer3));
    }

    @Test
    void getByIdBadScenario() {
        RepairerNotFoundException exception = Assertions.assertThrows(
                RepairerNotFoundException.class, () -> repairerService.getById(123)
        );
        Assert.assertEquals(INVALID_REPAIRER_ID + "123", exception.getMessage());
    }

    @Test
    void removeBadScenario() {
        RepairerNotFoundException exception = Assertions.assertThrows(
                RepairerNotFoundException.class, () -> repairerService.remove(321)
        );
        Assert.assertEquals(INVALID_REPAIRER_ID + 321, exception.getMessage());
    }
}