//
//  GithubService.swift
//
//  Created by Arunabh Das on 7/31/25.
//

import SwiftUI

// GitHub API Service
class GitHubService {
    func fetchUserProfile() async throws -> GitHubUser {
        // Create URL
        guard let url = URL(string: "https://api.github.com/users/arunabhdas") else {
            throw NetworkError.invalidURL
        }
        
        // Create URLRequest with headers (GitHub API often requires User-Agent)
        var request = URLRequest(url: url)
        request.setValue("GitHubAPI/1.0", forHTTPHeaderField: "User-Agent")
        
        // Make the network call using async/await
        let (data, response) = try await URLSession.shared.data(for: request)
        
        // Check if the response is valid
        guard let httpResponse = response as? HTTPURLResponse else {
            throw NetworkError.invalidResponse
        }
        
        guard httpResponse.statusCode == 200 else {
            throw NetworkError.invalidResponse
        }
        
        // Decode the response
        do {
            let decoder = JSONDecoder()
            let user = try decoder.decode(GitHubUser.self, from: data)
            return user
        } catch {
            throw NetworkError.decodingError
        }
    }
}

// Network error cases
enum NetworkError: Error {
    case invalidURL
    case invalidResponse
    case invalidData
    case decodingError
}
