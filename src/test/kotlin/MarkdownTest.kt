import com.github.valv.kotlin.dsl.markdown.md

fun main(args: Array<String>) {
    println(md {
        + "First"
        bold {
            + "Bold Text"
        }
        + "Then"
        italic {
            + "Italic Text"
        }
        list{
            marked { + "First" }
            marked { + "Second" }
            numbered { + "One" }
            numbered { + "Two" }
        }
        image { url = "none"; + "Alt Text" }
    })
}
