/*
 * File:        Markdown.kt
 * Description: Typesafe Markdown building
 * Author:      ValV
 * TODO: Add KDoc after complete implementation and test
 */

package com.github.valv.kotlin.dsl.markdown

import kotlin.text.StringBuilder

interface Element {
    fun render(builder: StringBuilder)
}

class TextElement(val text: String): Element {
    override fun render(builder: StringBuilder) {
        builder.append(text)
    }
}

@DslMarker
annotation class DownMarker

@DownMarker
abstract class Mark: Element {
    val content = arrayListOf<Element>()

    protected fun <T: Element> init(mark: T, init: T.() -> Unit): T {
        mark.init()
        content.add(mark)
        return mark
    }

    // TODO: Hide inside List class
    operator fun String.unaryPlus() {
        text(this)
    }

    // TODO: Hide inside List class (or remove permanently)
    fun text(text: String) {
        content.add(TextElement("$text "))
    }

    override fun render(builder: StringBuilder) {
        for (c in content) {
            c.render(builder)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder)
        return builder.toString()
    }
}

class Markdown: Mark() {
    fun bold(init: Bold.() -> Unit) = init(Bold(), init)
    fun italic(init: Italic.() -> Unit) = init(Italic(), init)
    fun list(init: List.() -> Unit) = init(List(), init)
    fun image(url: String = "", format: String = "", init: Image.() -> Unit) = init(Image(url, format), init)
    // TODO: Add link(), blockquote(), codeline(), codeblock()
}

class Bold: Mark() {
    override fun render(builder: StringBuilder) {
        builder.append("**")
        super.render(builder)
        builder.insert(builder.length - 1, "**")
    }
}

class Italic: Mark() {
    override fun render(builder: StringBuilder) {
        builder.append("_")
        super.render(builder)
        builder.insert(builder.length - 1, "_")
    }
}

class List: Mark() {
    fun marked(init: ListMarked.() -> Unit) = init(ListMarked(), init)
    fun numbered(init: ListNumbered.() -> Unit) = init(ListNumbered(), init)

    override fun render(builder: StringBuilder) {
        builder.append("\n")
        super.render(builder)
        builder.append("\n")
    }
}

class ListMarked: Mark() {
    override fun render(builder: StringBuilder) {
        builder.append("* ")
        super.render(builder)
        builder.setCharAt(builder.length - 1, '\n')
    }
}

class ListNumbered: Mark() {
    override fun render(builder: StringBuilder) {
        builder.append("0. ")
        super.render(builder)
        builder.setCharAt(builder.length - 1, '\n')
    }
}

class Image(var url: String, var format: String): Mark() {
    override fun render(builder: StringBuilder) {
        builder.append(if (format == "") "![" else "$format: ![")
        super.render(builder)
        builder.insert(builder.length - 1, "]($url)")
    }
}

// TODO: Write some code here just like Image class
class Link: Mark()

// TODO: Add override render
class Blockquote: Mark()

// TODO: Add code
class CodeInline: Mark()

// TODO: Check for declaration in basic Markdown
class CodeBlock: Mark()

fun md(init: Markdown.() -> Unit): Markdown {
    val markdown = Markdown()
    markdown.init()
    return markdown
}
