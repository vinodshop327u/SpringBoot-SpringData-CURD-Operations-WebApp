package com.web.bs.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.bs.model.Book;
import com.web.bs.service.BookService;

@Controller
public class BookController {

	@Autowired(required = true)
	private BookService bookService;

	@ModelAttribute("book")
	public Book bookForm() {
		return new Book();
	}

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/searchByIsbn")
	public String isbnSearchForm() {
		return "isbnSearchResults";
	}

	@RequestMapping(value = "/store", method = RequestMethod.POST)
	public String storeBook(Model model, @ModelAttribute("book") Book b) {
		// calling service method
		boolean isInserted = bookService.insert(b);

		if (isInserted) {
			model.addAttribute("sMsg", "Book Stored Successfully");
		} else {
			model.addAttribute("eMsg", "Failed to store");
		}

		return "index";
	}

	@RequestMapping(value = "/retriveBooks")
	public String viewBooks(Model model, HttpServletRequest req) {

		List<Book> booksList = null;

		String viewName = "";

		String isbn = req.getParameter("isbn");

		if (!"".equals(isbn) && null != isbn) {
			booksList = bookService.findByIsbn(isbn);
			viewName = "isbnSearchResults";
		} else {
			booksList = bookService.findAllBooks();
			viewName = "viewBooks";
		}

		model.addAttribute("books", booksList);
		return viewName;
	}

	@RequestMapping("/delete")
	public String deleteBook(Model model, HttpServletRequest req) {
		boolean isDeleted = false;
		String bid = req.getParameter("bid");
		if (bid != null && !bid.equals("")) {
			int bookId = Integer.parseInt(bid);
			isDeleted = bookService.delete(bookId);
			if (isDeleted) {
				model.addAttribute("sMsg", "Deleted Successfully..");
			} else {
				model.addAttribute("eMsg", "Failed to deleted..");
			}
		}
		return "redirect:retriveBooks";
	}

	@RequestMapping("/edit")
	public String edit(Model model, HttpServletRequest req) {

		String bid = req.getParameter("bid");

		if (null != bid && !"".equals(bid)) {
			int bookId = Integer.parseInt(bid);
			Book b = bookService.edit(bookId);
			model.addAttribute("book", b);
		}
		return "editBook";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Model model, @ModelAttribute("book") Book b) {
		boolean isUpdated = bookService.update(b);
		if (isUpdated) {
			model.addAttribute("sMsg", "Updated Successfully..");
		} else {
			model.addAttribute("eMsg", "Failed to update..");
		}
		return "editBook";
	}
}
//http://localhost:7070/SpringBootApp/index