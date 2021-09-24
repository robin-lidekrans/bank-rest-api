package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type bank struct {
	Key  string `json:key`
	Name string `json:name`
}

var banks = []bank{
	{Key: "1", Name: "SWEDBANK"},
	{Key: "2", Name: "IKANOBANKEN"},
	{Key: "3", Name: "JPMORGAN"},
	{Key: "4", Name: "NORDEA"},
	{Key: "5", Name: "CITIBANK"},
	{Key: "6", Name: "HANDELSBANKEN"},
	{Key: "7", Name: "SBAB"},
	{Key: "8", Name: "HSBC"},
	{Key: "9", Name: "NORDNET"},
}

func listBanks(c *gin.Context) {
	c.JSON(http.StatusOK, banks)
}

func getBankByKeyOrName(c *gin.Context) {
	query := c.Param("query")
	for _, x := range banks {
		if (x.Key == query) || (x.Name == query) {
			c.JSON(http.StatusOK, x)
			return
		}
	}
	c.JSON(http.StatusOK, nil)
}

func main() {
	router := gin.Default()

	endpoint := "/bank"

	router.GET(endpoint+"/list", listBanks)
	router.GET(endpoint+"/find.:query", getBankByKeyOrName)
	router.NoRoute(func(c *gin.Context) {
		c.JSON(http.StatusOK, nil)
	})
	
	router.Run("localhost:8070")
}
