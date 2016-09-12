package org.jboss.resteasy.specimpl;


import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.jboss.resteasy.resteasy_jaxrs.i18n.Messages;

import java.net.URI;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class LinkBuilderImpl implements Link.Builder
{
   protected LinkImpl link = new LinkImpl();
   protected UriBuilder uriBuilder;
   protected URI baseUri = null;

   @Override
   public Link.Builder link(Link link)
   {
      uriBuilder = UriBuilder.fromUri(link.getUri());
      this.link.map.clear();
      this.link.map.putAll(link.getParams());
      return this;
   }

   @Override
   public Link.Builder link(String link)
   {
      Link l = LinkImpl.valueOf(link);
      return link(l);
   }

   @Override
   public Link.Builder uriBuilder(UriBuilder uriBuilder)
   {
      this.uriBuilder = uriBuilder.clone();
      return this;
   }

   @Override
   public Link.Builder uri(URI uri) {
      if (uri == null) throw new IllegalArgumentException(Messages.MESSAGES.uriParamNull());
      uriBuilder = UriBuilder.fromUri(uri);
      return this;
   }

   @Override
   public Link.Builder uri(String uri) throws IllegalArgumentException {
      if (uri == null) throw new IllegalArgumentException(Messages.MESSAGES.uriParamNull());
      uriBuilder = UriBuilder.fromUri(uri);
      return this;
   }

   @Override
   public Link.Builder rel(String rel) {
      if (rel == null) throw new IllegalArgumentException(Messages.MESSAGES.relParamNull());
      final String rels = link.map.get(Link.REL);
      param(Link.REL, rels == null ? rel : rels + " " + rel);
      return this;
   }

   @Override
   public Link.Builder title(String title) {
      if (title == null) throw new IllegalArgumentException(Messages.MESSAGES.titleParamNull());
      param(Link.TITLE, title);
      return this;

   }

   @Override
   public Link.Builder type(String type) {
      if (type == null) throw new IllegalArgumentException(Messages.MESSAGES.typeParamNull());
      param(Link.TYPE, type);
      return this;
   }

   @Override
   public Link.Builder param(String name, String value) throws IllegalArgumentException {
      if (name == null) throw new IllegalArgumentException(Messages.MESSAGES.nameParamWasNull());
      if (value == null) throw new IllegalArgumentException(Messages.MESSAGES.valueParamWasNull());
      link.map.put(name, value);
      return this;
   }

   @Override
   public Link build(Object... values) throws UriBuilderException
   {
      if (values == null) throw new IllegalArgumentException(Messages.MESSAGES.valuesParamWasNull());
      link.uri = uriBuilder.build(values);
      return link;
   }

   @Override
   public Link buildRelativized(URI uri, Object... values)
   {
      if (uri == null) throw new IllegalArgumentException(Messages.MESSAGES.uriParamNull());
      if (values == null) throw new IllegalArgumentException(Messages.MESSAGES.valuesParamWasNull());
      URI built = uriBuilder.build(values);
      URI with = built;
      if (baseUri != null) with = baseUri.resolve(built);
      link.uri = uri.relativize(with);
      return link;
   }

   @Override
   public Link.Builder baseUri(URI uri)
   {
      this.baseUri = uri;
      return this;
   }

   @Override
   public Link.Builder baseUri(String uri)
   {
      this.baseUri = URI.create(uri);
      return this;
   }
}