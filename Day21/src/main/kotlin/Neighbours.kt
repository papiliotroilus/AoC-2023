fun Point4D.up(): Point4D { return Point4D(lat - 1, lon, metaLat, metaLon) }
fun Point4D.down(): Point4D { return Point4D(lat + 1, lon, metaLat, metaLon) }
fun Point4D.left(): Point4D { return Point4D(lat, lon - 1, metaLat, metaLon) }
fun Point4D.right(): Point4D { return Point4D(lat, lon + 1, metaLat, metaLon) }
fun Point4D.neighbours(): MutableList<Point4D> { return mutableListOf(up(), down(), left(), right()) }
fun MutableList<Point4D>.wrapped(maxLat: Int, maxLon: Int): MutableList<Point4D> {
    val newList = mutableListOf<Point4D>()
    newList.addAll(this)
    for (point in newList) {
        if (point.lat == -1) {
            point.lat = maxLat
            point.metaLat -= 1
        } else if (point.lat == maxLat + 1) {
            point.lat = 0
            point.metaLat += 1
        }
        if (point.lon == -1) {
            point.lon = maxLon
            point.metaLon -= 1
        } else if (point.lon == maxLon + 1) {
            point.lon = 0
            point.metaLon += 1
        }
    }
    return newList
}