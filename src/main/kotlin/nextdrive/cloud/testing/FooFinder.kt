package nextdrive.cloud.testing

interface FooFinder {
    /**
     * Find foo.json by label
     * return specific value if label exist
     * return null otherwise
     */
    fun find(label: String): String?
}
