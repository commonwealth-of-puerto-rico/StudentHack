package strudanthack

/**
 * SchoolClass
 * A domain class describes the data object and it's mapping to the database
 */
class SchoolClass {
    String className

    /* Default (injected) attributes of GORM */
//	Long	id
//	Long	version

    /* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated

	static belongsTo	= [school:School]	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
	static hasOne		= [school:School, teacher:Teacher]	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static hasMany		= [studens:Student]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static mappedBy		= [school]	// specifies which property should be used in a mapping

    static mapping = {
    }

    static constraints = {
        className(blank: false)
    }

    /*
     * Methods of the Domain Class
     */
	@Override	// Override toString for a nicer / more descriptive UI
    public String toString() {
        return "${className}";
    }
}
