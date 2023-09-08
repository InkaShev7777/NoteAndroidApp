using System;
using AndroidApi.Models;

namespace AndroidApi.Manager
{
	public class CategoryManager
	{
		private List<Category> categories;
        private DbA9e881Inka29shevch2929Context context;
        public CategoryManager()
		{
			this.categories = new List<Category>();
			context = new DbA9e881Inka29shevch2929Context();
		}
		public void AddCategory(string title)
		{
			this.context.Category.Add(new Category() { Title = title});
			this.context.SaveChanges();
		}
		public List<Category> GetCategories()
		{
			return this.context.Category.ToList();
		}
	}
}

