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

func main() {
	router := gin.Default()

	router.GET("/list", listBanks)

	router.Run("localhost:8070")
}
