import org.jooq.Binding
import org.jooq.BindingGetResultSetContext
import org.jooq.BindingGetSQLInputContext
import org.jooq.BindingGetStatementContext
import org.jooq.BindingRegisterContext
import org.jooq.BindingSQLContext
import org.jooq.BindingSetSQLOutputContext
import org.jooq.BindingSetStatementContext
import org.jooq.Converter
import org.jooq.impl.DSL.`val`
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types

data class Int8MultiRange(
    val value: List<LongRange> = emptyList()
) {
    constructor(vararg ranges: LongRange) : this(ranges.toList())
}

class Int8MultiRangeConverter : Converter<String, Int8MultiRange> {
    private val regex = "\\[(.*?),(.*?)\\)".toRegex()

    override fun from(databaseObject: String?): Int8MultiRange? {
        return databaseObject?.let {
            val matches = regex.findAll(databaseObject.toString())
            Int8MultiRange(
                matches.map {
                    val (start, end) = it.destructured
                    LongRange(
                        start.toLong(),
                        end.toLong() - 1
                    )
                }.toList()
            )
        }
    }

    override fun to(userObject: Int8MultiRange?): String? {
        return userObject?.let { ranges ->
            ranges.value.joinToString(separator = ",", prefix = "{", postfix = "}") {
                "[${it.first},${it.last + 1})"
            }
        }
    }

    override fun fromType(): Class<String> {
        return String::class.java
    }

    override fun toType(): Class<Int8MultiRange> {
        return Int8MultiRange::class.java
    }
}

class Int8MultiRangeBinding : Binding<String, Int8MultiRange> {
    override fun converter(): Converter<String, Int8MultiRange> {
        return Int8MultiRangeConverter()
    }

    override fun sql(ctx: BindingSQLContext<Int8MultiRange>) {
        ctx.render()
            .visit(`val`(ctx.convert(converter()).value()))
            .sql("::int8multirange")
    }

    override fun register(ctx: BindingRegisterContext<Int8MultiRange>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR)
    }

    override fun set(ctx: BindingSetStatementContext<Int8MultiRange>) {
        ctx.statement().setString(ctx.index(), ctx.convert(converter()).value().toString())
    }

    override fun set(ctx: BindingSetSQLOutputContext<Int8MultiRange>) {
        ctx.output().writeString(ctx.convert(converter()).value().toString())
    }

    override fun get(ctx: BindingGetResultSetContext<Int8MultiRange>) {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
    }

    override fun get(ctx: BindingGetStatementContext<Int8MultiRange>) {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
    }

    override fun get(ctx: BindingGetSQLInputContext<Int8MultiRange>) {
        throw SQLFeatureNotSupportedException()
    }
}
