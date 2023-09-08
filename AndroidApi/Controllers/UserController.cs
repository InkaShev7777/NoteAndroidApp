using System;
using AndroidApi.Manager;
using AndroidApi.Models;
using Microsoft.AspNetCore.Mvc;

namespace AndroidApi.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UserController:ControllerBase
	{
		private List<AspNetUser> Users;
        private DbA9e881Inka29shevch2929Context context;
		private NoteManager noteManager;
		private CategoryManager categoryManager;
        public UserController()
		{
			this.Users = new List<AspNetUser>();
			this.context = new DbA9e881Inka29shevch2929Context();
			this.noteManager = new NoteManager();
			this.categoryManager = new CategoryManager();
		}

		//
		//		GET
		//
		[HttpGet]
		[Route("GetNotes")]
		public List<Note> GetAllNotes(int idCategory)
		{
			return this.noteManager.ShowAllNote(idCategory);
		}
        [HttpGet]
        [Route("SearchNotes")]
        public List<Note> SearchNotes(string searchtext, int idCate)
        {
            return this.noteManager.Search(searchtext, idCate);
        }
        [HttpGet]
        [Route("getCat")]
        public List<Category> GetCategories()
        {
            return this.categoryManager.GetCategories();
        }
		//
		//		POST
		//
        [HttpPost]
		[Route("AddNote")]
		public void AddNote(string title,string body,int id_category)
		{
			this.noteManager.AddNote(title, body,id_category);
		}
		
		[HttpPost]
		[Route("AddCategory")]
		public void AddCategory(string title)
		{
			this.categoryManager.AddCategory(title);
		}
    }
}

