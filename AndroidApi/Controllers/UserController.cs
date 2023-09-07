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
		private NoteManager manager;
        public UserController()
		{
			this.Users = new List<AspNetUser>();
			this.context = new DbA9e881Inka29shevch2929Context();
			this.manager = new NoteManager();
		}
		[HttpGet]
		[Route("GetNotes")]
		public List<Note> GetAllNotes()
		{
			return this.manager.ShowAllNote();
		}
		[HttpPost]
		[Route("AddNote")]
		public void AddNote(string title,string body,int id_category)
		{
			this.manager.AddNote(title, body,id_category);
		}
		[HttpGet]
		[Route("SearchNotes")]
		public List<Note> SearchNotes(string searchtext)
		{
			return this.manager.Search(searchtext);
		}
		[HttpGet]
		[Route("getCat")]
		public List<Category> GetCategories()
		{
			return this.context.Category.ToList();
		}
    }
}

