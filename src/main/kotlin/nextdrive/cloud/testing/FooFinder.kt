package nextdrive.cloud.testing

interface FooFinder {
    /**
     * Find foo.json by label
     * return specific value if label exist
     * return null otherwise
     */
    fun find(label: String): String?
}

class NextDrive(label: String) : FooFinder{
    val mapOfLabel = mapOf(
        "eb947b41-1fc8-420a-95bf-404d2fb01a1b" to "0e0dfed3-79d4-46e5-a0b1-4645b42f4aee",
        "423e5d8e-7d02-4b0c-b229-79683559d4fb" to "6fe58c5b-1f68-4b14-aa0e-9270b5f48153",
        "8fc5d53a-df9e-4f73-8a36-5901e8ae0de3" to "09a6c4a4-eecd-4445-8b26-301ab2f8f391",
        "18aef181-5e7e-4c2c-b5e6-8ca57e0e4a14" to "d1c647f9-afd4-416b-bb45-bd27693b5f9c",
        "06e189fa-e93f-4139-93cd-d9dbdb2dbe0c" to "f66385e0-aa46-4d03-840c-0bc41135f7a8"
    )

    override fun find(label: String): String{
        if(mapOfLabel.containsKey(label)){
            return mapOfLabel.get(label)
        }else{
            return null
        } 
    }

}
