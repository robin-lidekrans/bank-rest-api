package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type person struct {
	Key  string `json:key`
	Name string `json:name`
}

var persons = []person{
	{Key: "1", Name: "Jakob Pogulis"},
	{Key: "2", Name: "Xena"},
	{Key: "3", Name: "Marcus Bendtsen"},
	{Key: "4", Name: "Zorro"},
	{Key: "5", Name: "Q"},
}

func listPersons(c *gin.Context) {
	c.JSON(http.StatusOK, persons)
}

func getPersonByKeyOrName(c *gin.Context) {
	query := c.Param("query")
	for _, x := range persons {
		if (x.Key == query) || (x.Name == query) {
			c.JSON(http.StatusOK, x)
			return
		}
	}
	c.JSON(http.StatusNotFound, nil)
}

func main() {
	router := gin.Default()

	endpoint := "/person"

	router.GET(endpoint+"/list", listPersons)
	router.GET(endpoint+"/list.:query", getPersonByKeyOrName)

	router.Run("localhost:8060")
}
