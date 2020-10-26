import composite.id.jpa.CompositeIdDomain
import composite.id.jpa.DomainOne
import composite.id.jpa.DomainTwo
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.hibernate.SessionFactory
import spock.lang.Specification

@Rollback
@Integration
class CompositeIdQuerySpec extends Specification {

  SessionFactory sessionFactory

  def "test query on composite id using findBy"() {
    // Save objects
    def domainOne = new DomainOne().save(failOnError:true)
    def domainTwo = new DomainTwo().save(failOnError:true)
    def composite = new CompositeIdDomain(domainOne:domainOne, domainTwo:domainTwo, otherProperty:"foo").save(failOnError:true, flush:true)

    expect:
    CompositeIdDomain.count() == 1
    CompositeIdDomain.findByDomainOne(domainOne) == composite
  }

  def "test empty JPA query"() {
    // Save objects
    def domainOne = new DomainOne().save(failOnError:true)
    def domainTwo = new DomainTwo().save(failOnError:true)
    def composite = new CompositeIdDomain(domainOne:domainOne, domainTwo:domainTwo, otherProperty:"foo").save(failOnError:true, flush:true)

    // Construct empty query selecting all composite domains
    def criteriaBuilder = sessionFactory.criteriaBuilder
    def criteriaQuery = criteriaBuilder.createQuery()
    criteriaQuery.select(criteriaQuery.from(CompositeIdDomain))
    def query = sessionFactory.currentSession.createQuery(criteriaQuery)

    expect:
    query.list() == [composite]
  }

  /**
   * Show that queries using JPA work in general
   */
  def "test JPA query with condition on otherProperty == foo"() {
    // Save objects
    def domainOne = new DomainOne().save(failOnError:true)
    def domainTwo = new DomainTwo().save(failOnError:true)
    def composite = new CompositeIdDomain(domainOne:domainOne, domainTwo:domainTwo, otherProperty:"foo").save(failOnError:true, flush:true)

    // Construct query on otherProperty == foo
    def criteriaBuilder = sessionFactory.criteriaBuilder
    def criteriaQuery = criteriaBuilder.createQuery()
    def root = criteriaQuery.from(CompositeIdDomain)
    criteriaQuery.select(root)
    criteriaQuery.where(criteriaBuilder.equal(root.get("otherProperty"), "foo"))
    def query = sessionFactory.currentSession.createQuery(criteriaQuery)

    expect:
    query.list() == [composite]
  }

  def "test JPA query with condition on otherProperty == bar"() {
    // Save objects
    def domainOne = new DomainOne().save(failOnError:true)
    def domainTwo = new DomainTwo().save(failOnError:true)
    def composite = new CompositeIdDomain(domainOne:domainOne, domainTwo:domainTwo, otherProperty:"foo").save(failOnError:true, flush:true)

    // Construct query on otherProperty == bar
    def criteriaBuilder = sessionFactory.criteriaBuilder
    def criteriaQuery = criteriaBuilder.createQuery()
    def root = criteriaQuery.from(CompositeIdDomain)
    criteriaQuery.select(root)
    criteriaQuery.where(criteriaBuilder.equal(root.get("otherProperty"), "bar"))
    def query = sessionFactory.currentSession.createQuery(criteriaQuery)

    expect:
    query.list() == []
  }

  def "test JPA query with condition on domainOne"() {
    // Save objects
    def domainOne = new DomainOne().save(failOnError:true)
    def domainTwo = new DomainTwo().save(failOnError:true)
    def composite = new CompositeIdDomain(domainOne:domainOne, domainTwo:domainTwo, otherProperty:"foo").save(failOnError:true, flush:true)

    // Construct query on otherProperty == bar
    def criteriaBuilder = sessionFactory.criteriaBuilder
    def criteriaQuery = criteriaBuilder.createQuery()
    def root = criteriaQuery.from(CompositeIdDomain)
    criteriaQuery.select(root)
    criteriaQuery.where(criteriaBuilder.equal(root.get("domainOne"), domainOne))
    def query = sessionFactory.currentSession.createQuery(criteriaQuery)

    expect:
    query.list() == [composite]
  }
}
