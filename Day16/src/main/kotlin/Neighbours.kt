fun Point2D.up(): Point2D { return Point2D(lat - 1, lon) }
fun Point2D.down(): Point2D { return Point2D(lat + 1, lon) }
fun Point2D.left(): Point2D { return Point2D(lat, lon - 1) }
fun Point2D.right(): Point2D { return Point2D(lat, lon + 1) }
fun Point2D.neighbours(): MutableList<Point2D> { return mutableListOf(up(), down(), left(), right()) }
fun Point2D.check(maxLat: Int, maxLon: Int): Boolean { return lat in 0..maxLat && lon in 0..maxLon }
fun MutableList<Point2D>.culled(maxLat: Int, maxLon: Int): MutableList<Point2D> {
    return this.filter { it.lat >= 0 && it.lon >= 0 && it.lat <= maxLat && it.lon <= maxLon }.toMutableList()
}