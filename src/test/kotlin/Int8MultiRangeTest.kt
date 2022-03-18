import org.jooq.impl.DSL.*
import org.jooq.impl.SQLDataType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
class Int8MultiRangeTest {
    @Container
    val postgres14 = PostgreSQLContainer(DockerImageName.parse("postgres:14.2"))
        .waitingFor(HostPortWaitStrategy())
        .withInitScript("init.sql")

    private val testTable = table("test")
    private val rangesField = field(
        name("test", "ranges"),
        SQLDataType.VARCHAR.asConvertedDataType(Int8MultiRangeBinding())
    )

    @Test
    fun `Insert into a column of type int8multirange`() {
        val ranges = Int8MultiRange(
            LongRange(50, 100),
            LongRange(200, 300)
        )

        val inserted = using(
            postgres14.jdbcUrl,
            postgres14.username,
            postgres14.password
        ).use { ctx ->
            ctx
                .insertInto(testTable)
                .columns(rangesField)
                .values(ranges)
                .execute()
        }

        assertEquals(1, inserted)
    }

    @Test
    fun `Select from a column of type int8multirange`() {
        val ranges = using(
            postgres14.jdbcUrl,
            postgres14.username,
            postgres14.password
        ).use { ctx ->
            ctx
                .select(
                    rangesField
                )
                .from(testTable)
                .fetch(rangesField)
        }

        assertEquals(1, ranges[0].value[0].first)
        assertEquals(10, ranges[0].value[0].last)
        assertEquals(12, ranges[0].value[1].first)
        assertEquals(20, ranges[0].value[1].last)
        assertEquals(1, ranges[1].value[0].first)
        assertEquals(5, ranges[1].value[0].last)
        assertEquals(7, ranges[1].value[1].first)
        assertEquals(13, ranges[1].value[1].last)
    }
}
