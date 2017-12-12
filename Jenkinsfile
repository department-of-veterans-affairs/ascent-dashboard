@Library('ascent') _

microservicePipeline {
    imageName = 'ascent/ascent-dashboard'

    /*
    Define a mapping of environment variables that will be populated with Vault token values
    from the associated vault token role
    */
    vaultTokens = [
        "VAULT_TOKEN": "ascent-platform"
    ]
    testEnvironment = ['docker-compose.yml']
    serviceToTest = 'ascent-dashboard'
    deployWaitTime = 60
    testVaultTokenRole = "ascent-platform"
}