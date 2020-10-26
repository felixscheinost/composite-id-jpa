package composite.id.jpa

/**
 * This is a simple domain class that has a composite ID of two other domain classes
 */
class CompositeIdDomain implements Serializable {

  DomainOne domainOne
  DomainTwo domainTwo
  String otherProperty

  static constraints = {
    id composite:["domainOne", "domainTwo"]
  }
}
