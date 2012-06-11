/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.formatting.partition;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TypedPosition;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.InvalidFormatPositionException;

@RunWith(JMock.class)
public class PartitionedFormattingContextTest {

	private Mockery context;

	private ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	@Before
	public void initialise() {
		this.context = new Mockery();

		this.contentTypeDefinitionFactory = context
				.mock(ContentTypeDefinitionFactory.class);
	}

	@Test
	public void hasPreviousContentTypeReturnsFalseForFirstLine()
			throws Exception {

		final TypedPosition[] positions = positions(position(0, 10,
				"position-1"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		assertFalse(formattingContext.hasPreviousContentType());
	}

	@Test
	public void hasPreviousContentTypeReturnsFalseIfOnlyPreviousTypeIsWhitespace()
			throws Exception {
		final TypedPosition[] positions = positions(
				position(0, 10, IDocument.DEFAULT_CONTENT_TYPE),
				position(11, 10, "position-1"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 1);

		assertFalse(formattingContext.hasPreviousContentType());
	}

	@Test
	public void hasPreviousContentTypeReturnsTrueForSecondType()
			throws Exception {
		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"), position(11, 10, "position-2"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 1);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-1");

			}
		});

		assertTrue(formattingContext.hasPreviousContentType());
	}

	@Test(expected = InvalidFormatPositionException.class)
	public void previousContentTypeThrowsExceptionForFirstContentType()
			throws BadLocationException {

		final TypedPosition[] positions = positions(position(0, 10,
				"position-1"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		formattingContext.previousContentType();
	}

	@Test
	public void previousContentTypeReturnsCorrectContentType() throws Exception {
		final ContentTypeDefinition previousContentType = context
				.mock(ContentTypeDefinition.class);

		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"), position(11, 10, "position-2"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 1);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-1");
				will(returnValue(previousContentType));
			}
		});

		assertThat(formattingContext.previousContentType(),
				is(previousContentType));
	}

	@Test
	public void previousContentTypeReturnsCorrectNonWhitespace()
			throws Exception {
		final ContentTypeDefinition previousContentType = context.mock(
				ContentTypeDefinition.class, "previousContentType");

		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"),
				position(11, 10, IDocument.DEFAULT_CONTENT_TYPE),
				position(21, 10, "position-3"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 2);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-1");
				will(returnValue(previousContentType));
			}
		});

		assertThat(formattingContext.previousContentType(),
				is(previousContentType));
	}

	@Test
	public void hasNextContentTypeReturnsFalseForLastLine() throws Exception {
		final TypedPosition[] positions = positions(position(0, 10,
				"position-1"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		assertFalse(formattingContext.hasNextContentType());
	}

	@Test
	public void hasNextContentTypeReturnsFalseIfOnlyNextTypeIsWhitespace()
			throws Exception {
		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"),
				position(11, 10, IDocument.DEFAULT_CONTENT_TYPE));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		assertFalse(formattingContext.hasNextContentType());
	}

	@Test
	public void hasNextContentTypeReturnsTrueForSecondLastType()
			throws Exception {
		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"), position(11, 10, "position-2"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-2");

			}
		});

		assertTrue(formattingContext.hasNextContentType());
	}

	@Test(expected = InvalidFormatPositionException.class)
	public void nextContentTypeThrowsExceptionForLastContentType()
			throws BadLocationException {
		final TypedPosition[] positions = positions(position(0, 10,
				"position-1"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		formattingContext.nextContentType();
	}

	@Test
	public void nextContentTypeReturnsCorrectContentType() throws Exception {
		final ContentTypeDefinition nextContentType = context
				.mock(ContentTypeDefinition.class);

		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"), position(11, 10, "position-2"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-2");
				will(returnValue(nextContentType));
			}
		});

		assertThat(formattingContext.nextContentType(), is(nextContentType));
	}

	@Test
	public void nextContentTypeReturnsCorrectNonWhitespace() throws Exception {
		final ContentTypeDefinition nextContentType = context.mock(
				ContentTypeDefinition.class, "nextContentType");

		final TypedPosition[] positions = positions(
				position(0, 10, "position-1"),
				position(11, 10, IDocument.DEFAULT_CONTENT_TYPE),
				position(21, 10, "position-2"));
		final FormattingContext formattingContext = formattingContextForPosition(
				positions, 0);

		context.checking(new Expectations() {
			{
				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"position-2");
				will(returnValue(nextContentType));
			}
		});

		assertThat(formattingContext.nextContentType(), is(nextContentType));
	}

	private FormattingContext formattingContextForPosition(
			final TypedPosition[] positions, final int currentPosition) {
		return new PartitionedFormattingContext(positions, currentPosition,
				contentTypeDefinitionFactory);
	}

	private TypedPosition[] positions(final TypedPosition... positions) {
		return positions;
	}

	private TypedPosition position(final int offset, final int length,
			final String type) {
		return new TypedPosition(offset, length, type);
	}
}
