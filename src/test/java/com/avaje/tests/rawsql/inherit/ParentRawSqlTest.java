package com.avaje.tests.rawsql.inherit;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import org.avaje.test.model.rawsql.inherit.ChildA;
import org.avaje.test.model.rawsql.inherit.ChildB;
import org.avaje.test.model.rawsql.inherit.Data;
import org.avaje.test.model.rawsql.inherit.Parent;
import org.avaje.test.model.rawsql.inherit.ParentAggregate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParentRawSqlTest extends BaseTestCase {

  @Test
  public void RawSqlParentLoad() {

    List<Data> exampleData = new ArrayList<Data>();
    exampleData.add(new Data(0));
    exampleData.add(new Data(1));
    exampleData.add(new Data(2));

    ChildA a = new ChildA(0);
    a.setData(exampleData);
    Ebean.save(a);

    ChildB b = new ChildB(1);
    b.setData(exampleData);
    Ebean.save(b);

    ChildA c = new ChildA(2);
    c.setData(exampleData);
    Ebean.save(c);

    RawSql rawSql = RawSqlBuilder
        .parse("select type, id, number from rawinherit_parent where number > 1")
        .create();

    List<Parent> partial = Ebean.find(Parent.class)
        .setRawSql(rawSql)
        .findList();

    assertNotNull(partial.get(0).getData());
    assertEquals(partial.get(0).getData().get(0).getNumber().intValue(), 0);
  }

  @Test
  public void RawSqlParentFetch() {

    List<Data> exampleData = new ArrayList<Data>();
    exampleData.add(new Data(0));
    exampleData.add(new Data(1));
    exampleData.add(new Data(2));

    ChildA a = new ChildA(0);
    a.setData(exampleData);
    Ebean.save(a);

    ChildB b = new ChildB(1);
    b.setData(exampleData);
    Ebean.save(b);

    ChildA c = new ChildA(2);
    c.setData(exampleData);
    Ebean.save(c);

    useColumnMappingIgnore();

    useColumnMappingWithDiscriminator();

    useExtraColumnMappingIgnore();
  }

  /**
   * Map the discriminator column - columnMapping("type", "parent.type").
   */
  private void useColumnMappingWithDiscriminator() {

    RawSql rawSql = RawSqlBuilder
        .unparsed("select type, id from rawinherit_parent where number > 1")
        .columnMapping("type", "parent.type") // can map the discriminator column 'type'
        .columnMapping("id", "parent.id")
        .create();

    List<ParentAggregate> aggregates = Ebean.find(ParentAggregate.class).setRawSql(rawSql)
        .fetch("parent", new FetchConfig().query())
        .findList();

    List<Parent> partial = new ArrayList<Parent>();
    for (ParentAggregate aggregate : aggregates) {
      partial.add(aggregate.parent);
    }

    assertNotNull(partial.get(0).getData());
    assertEquals(partial.get(0).getData().get(0).getNumber().intValue(), 0);
  }

  /**
   * Use columnMappingIgnore() to ignore the discriminator column.
   */
  private void useColumnMappingIgnore() {

    RawSql rawSql = RawSqlBuilder
        .unparsed("select type, id from rawinherit_parent where number > 1")
        .columnMappingIgnore("type") // can ignore the discriminator 'type'
        .columnMapping("id", "parent.id")
        .create();

    List<ParentAggregate> aggregates = Ebean.find(ParentAggregate.class).setRawSql(rawSql)
        .fetch("parent", new FetchConfig().query())
        .findList();

    List<Parent> partial = new ArrayList<Parent>();
    for (ParentAggregate aggregate : aggregates) {
      partial.add(aggregate.parent);
    }

    assertNotNull(partial.get(0).getData());
    assertEquals(partial.get(0).getData().get(0).getNumber().intValue(), 0);
  }

  /**
   * Extra columnMappingIgnore() before and after.
   */
  private void useExtraColumnMappingIgnore() {

    RawSql rawSql = RawSqlBuilder
        .unparsed("select 'a', type, id, 'b' from rawinherit_parent where number > 1")
        .columnMappingIgnore("a") // extra ignore before
        .columnMappingIgnore("type")
        .columnMapping("id", "parent.id")
        .columnMappingIgnore("b") // extra ignore after
        .create();

    List<ParentAggregate> aggregates = Ebean.find(ParentAggregate.class).setRawSql(rawSql)
        .fetch("parent", new FetchConfig().query())
        .findList();

    List<Parent> partial = new ArrayList<Parent>();
    for (ParentAggregate aggregate : aggregates) {
      partial.add(aggregate.parent);
    }
    assertNotNull(partial.get(0).getData());
    assertEquals(partial.get(0).getData().get(0).getNumber().intValue(), 0);
  }

}
