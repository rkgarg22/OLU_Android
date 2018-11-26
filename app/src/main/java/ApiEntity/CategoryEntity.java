package ApiEntity;

import java.util.List;

import ApiObject.CategoriesListObject;

/**
 * Created by Ankit Chhabra on 8/7/2018.
 */

public class CategoryEntity {

    List<CategoriesListObject> categoriesListObjectList;

    public CategoryEntity(List<CategoriesListObject> categoriesListObjectList) {
        this.categoriesListObjectList = categoriesListObjectList;
    }

    public List<CategoriesListObject> getCategoriesListObjectList() {
        return categoriesListObjectList;
    }

    public void setCategoriesListObjectList(List<CategoriesListObject> categoriesListObjectList) {
        this.categoriesListObjectList = categoriesListObjectList;
    }
}
