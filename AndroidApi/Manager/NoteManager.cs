using System;
using Microsoft.AspNetCore.Mvc;

namespace AndroidApi.Manager
{
    public class NoteManager
    {
        
        private DbA9e881Inka29shevch2929Context context;
		private List<Note> Notes;
		public NoteManager()
		{
			this.context = new DbA9e881Inka29shevch2929Context();
            this.Notes = this.context.Notes.ToList();
        }
		
		public List<Note> ShowAllNote(int idCategory)
		{
			return this.Notes.Where((x)=>x.IDCategory.Equals(idCategory)).ToList();
		}
		
		public void AddNote(string title,string body, int id_category)
		{
			this.context.Notes.Add(new Note { Title=title,Body=body,IDCategory=id_category});
			this.context.SaveChanges();
		}
		public List<Note> Search(string searchText,int idCat)
		{
			List<Note> SearchNotes = new List<Note>();
			if(searchText.Length > 0)
			{
                foreach (var item in this.Notes)
                {
                    if (item.IDCategory == idCat &&(item.Title.ToLower().Contains(searchText.ToLower()) || item.Body.ToLower().Contains(searchText.ToLower())))
                    {
                        SearchNotes.Add(item);
                    }
                }
                return SearchNotes;
            }
			else
			{
				return this.Notes;
			}
		}
		// delete
	}
}

